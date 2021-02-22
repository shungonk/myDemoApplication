package com.example.mydemo.web.model;

import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;

import com.example.mydemo.domain.entity.WalletEntity;

import org.bitcoinj.core.Base58;

public class Wallet {
    
    private String name;
    private String address;
    private String privateKey;
    private String publicKey;
    private boolean mine;
    private float balance;

    public Wallet(String name, String address, String privateKey, String publicKey, boolean mine) {
        this.name = name;
        this.address = address;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.mine = mine;
    }

    public static Wallet fromEntity(WalletEntity e) {
        if (e == null) return null;
        return new Wallet(e.getName(), e.getAddress(), e.getPrivateKey(), e.getPublicKey(), e.isMine());
    }

    public WalletEntity toEntity(String username) {
        return new WalletEntity(name, username, address, privateKey, publicKey, mine);
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public boolean isMine() {
        return mine;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public static Wallet create(String name) {
        try {
            // create the KeyPair, from which you can obtain the public and private keys.
            var keyGenerator = KeyPairGenerator.getInstance("EC"); //TODO: what the difference between getInstance("EC") and getInstance("ECDSA", "BC")?
            var ecGenSpec = new ECGenParameterSpec("secp256k1");
            keyGenerator.initialize(ecGenSpec);
            var keyPair = keyGenerator.genKeyPair();
            var pvt = keyPair.getPrivate();
            var pub = keyPair.getPublic();

            // store just the private part of the key since the public key can be derived from the private key.
            // The static method adjustTo64() merely pads the hex string with leading 0s so the total length is 64 characters.
            var epvt = (ECPrivateKey) pvt;
            var sepvt = adjustTo64(epvt.getS().toString(16)).toUpperCase();

            // The public part of the key generated above is encoded into a bitcoin address.
            // the ECDSA public key is represented by a point on an elliptical curve.
            // They are concatenated together with “04” at the beginning to represent the public key.
            var epub = (ECPublicKey)pub;
            var pt = epub.getW();
            var sx = adjustTo64(pt.getAffineX().toString(16)).toUpperCase();
            var sy = adjustTo64(pt.getAffineY().toString(16)).toUpperCase();
            var bcPub = "04" + sx + sy;

            // perform a SHA-256 digest on the public key, followed by a RIPEMD-160 digest.
            // We use the Bouncy Castle provider for performing the RIPEMD-160 digest since JCE does not implement this algorithm.
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] s1 = sha.digest(bcPub.getBytes("UTF-8"));
            MessageDigest rmd = MessageDigest.getInstance("RipeMD160", "BC");
            byte[] r1 = rmd.digest(s1);
        
            // add a version byte of 0x00 at the beginning of the hash.
            byte[] r2 = new byte[r1.length + 1];
            r2[0] = 0;
            for (int i = 0 ; i < r1.length ; i++) r2[i+1] = r1[i];
            
            // perform a SHA-256 hash twice on the result above.
            byte[] s2 = sha.digest(r2);
            byte[] s3 = sha.digest(s2);

            // The first 4 bytes of the result of the second hashing is used as the address checksum.
            // It is appended to the RIPEMD160 hash above. This is the 25-byte bitcoin address.
            byte[] a1 = new byte[25];
            for (int i = 0 ; i < r2.length ; i++) a1[i] = r2[i];
            for (int i = 0 ; i < 5 ; i++) a1[20 + i] = s3[i];

            // now use the Base58.encode() method from the bitcoinj library to arrive at the final bitcoin address.
            // This is the address to which the bitcoin should be sent to in a transaction.
            var address = Base58.encode(a1);

            return new Wallet(
                name,
                address, 
                Base64.getEncoder().encodeToString(pvt.getEncoded()), 
                Base64.getEncoder().encodeToString(pub.getEncoded()),
                false);
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String adjustTo64(String s) {
        switch (s.length()) {
            case 62: return "00" + s;
            case 63: return "0" + s;
            case 64: return s;
            default:
                throw new IllegalArgumentException("not a valid key: " + s);
        }
    }
}
