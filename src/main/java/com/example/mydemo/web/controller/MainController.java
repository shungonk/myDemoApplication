package com.example.mydemo.web.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;

import com.example.mydemo.config.BlockchainServerProperties;
import com.example.mydemo.domain.service.WalletService;
import com.example.mydemo.util.SecurityUtil;
import com.example.mydemo.util.StringUtil;
import com.example.mydemo.web.form.TransactionForm;
import com.example.mydemo.web.model.Wallet;
import com.example.mydemo.web.request.PurchaseRequest;
import com.example.mydemo.web.request.TransactionRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class MainController {

    @Autowired
    BlockchainServerProperties bcsProperties;

    @Autowired
    WalletService walletService;
    
    @GetMapping(value="/home")
    public String home(Authentication auth, Model model) {
        var user = (User) auth.getPrincipal();
        var walletList = walletService.findByUsername(user.getUsername());
        for (var wallet: walletList) {
            var res = getBalance(wallet.getAddress());
            var balanceStr = StringUtil.valueInJson(res.getBody(), "balance");
            wallet.setBalanceStr(balanceStr);
        }
        model.addAttribute("walletList", walletList);
        return "home";
    }
    
    @GetMapping(value="/wallet")
    public String wallet(@RequestParam("name") String name, Authentication auth, Model model) {
        var user = (User) auth.getPrincipal();
        var wallet = walletService.findByNameAndUsername(name, user.getUsername());            
        var res = getBalance(wallet.getAddress());
        var balanceStr = StringUtil.valueInJson(res.getBody(), "balance");
        wallet.setBalanceStr(balanceStr);
        model.addAttribute("wallet", wallet);
        return "wallet";
    }

    @ResponseBody
    @PostMapping(value="/wallet/new", produces = MediaType.APPLICATION_JSON_VALUE)
    public String createWallet(@RequestParam("name") String name, Authentication auth, Model model) {
        var user = (User) auth.getPrincipal();
        var count = walletService.countByNameAndUsername(name, user.getUsername());
        if (count != 0) {
            return StringUtil.singleEntryJson("message", "Error: Same Name Already Exists.");
        }
        var newWallet = Wallet.create(name);

        // create purchase request
        var pvt = newWallet.getPrivateKey();
        var timestamp = Instant.now().toEpochMilli();
        var value = new BigDecimal("30");
        var data = newWallet.getAddress() + value.toPlainString() + Long.toString(timestamp);
        var request = new PurchaseRequest(
            newWallet.getPublicKey(), 
            newWallet.getAddress(), 
            value,
            timestamp,
            SecurityUtil.createEcdsaSign(pvt, data));

        var res = purchase(request);
        if (res.getStatusCodeValue() != 201) {
            return StringUtil.singleEntryJson("message", "FAILED: Failed to create");
        } 
        walletService.save(user.getUsername(), newWallet);
        return StringUtil.singleEntryJson("message", "SUCCESS: Created!");
    }

    @ResponseBody
    @GetMapping(value="/balance")
    public ResponseEntity<String> getBalance(@RequestParam("address") String address) {
        try {
            var client = new RestTemplate(new SimpleClientHttpRequestFactory());
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            var uri = new URI(String.format("http://%s:%s/balance",
                bcsProperties.getHost(), bcsProperties.getPort()));
            var queryUri = UriComponentsBuilder
                .fromUri(uri)
                .queryParam("address", address)
                .build().encode().toUri();
            var req = new RequestEntity<>(headers, HttpMethod.GET, queryUri);
            return client.exchange(req, String.class);

        } catch (HttpClientErrorException e) {
            return ResponseEntity
                .status(e.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(e.getResponseBodyAsString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(StringUtil.doubleEntryJson(
                    "message", "ERROR: Internal Server Error",
                    "balance", "0.000000"));
        } catch (RestClientException e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(StringUtil.doubleEntryJson(
                    "message", "ERROR: External API Error",
                    "balance", "0.000000"));
        }
    }

    @ResponseBody
    @PostMapping(value="/transaction", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendTransaction(@RequestBody TransactionForm form) {
        // create transaction request
        var senderPvt = form.getSenderPrivateKey();
        var timestamp = Instant.now().toEpochMilli();
        var data = form.getSenderAddress() + form.getRecipientAddress() + form.getValue().toPlainString() + Long.toString(timestamp);
        var transactionReq = new TransactionRequest(
            form.getSenderPublicKey(), 
            form.getSenderAddress(), 
            form.getRecipientAddress(), 
            form.getValue(),
            timestamp,
            SecurityUtil.createEcdsaSign(senderPvt, data));

        try {
            var client = new RestTemplate(new SimpleClientHttpRequestFactory());
            var uri = new URI(String.format("http://%s:%s/transaction",
                bcsProperties.getHost(), bcsProperties.getPort()));
            var request = RequestEntity
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(transactionReq.marshalJson());
            return client.exchange(request, String.class);

        } catch (HttpClientErrorException e) {
            return ResponseEntity
                .status(e.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(e.getResponseBodyAsString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(StringUtil.singleEntryJson(
                    "message", "ERROR: Internal Server Error"));
        } catch (RestClientException e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(StringUtil.singleEntryJson(
                    "message", "ERROR: External API Error"));
        }
    }

    @ResponseBody
    @PostMapping(value="/mine")
    public ResponseEntity<String> mine(@RequestParam("address") String address) {
        try {
            var client = new RestTemplate(new SimpleClientHttpRequestFactory());
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            var uri = new URI(String.format("http://%s:%s/mine",
                bcsProperties.getHost(), bcsProperties.getPort()));
            var queryURI = UriComponentsBuilder
                .fromUri(uri)
                .queryParam("address", address)
                .build().encode().toUri();
            var req = new RequestEntity<>(headers, HttpMethod.POST, queryURI);
            return client.exchange(req, String.class);

        } catch (HttpClientErrorException e) {
            return ResponseEntity
                .status(e.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(e.getResponseBodyAsString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(StringUtil.singleEntryJson(
                    "message", "ERROR: Internal Server Error"));
        } catch (RestClientException e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(StringUtil.singleEntryJson(
                    "message", "ERROR: External API Error"));
        }
    }

    public ResponseEntity<String> purchase(PurchaseRequest purchase) {
        try {
            var client = new RestTemplate(new SimpleClientHttpRequestFactory());
            var uri = new URI(String.format("http://%s:%s/purchase",
                bcsProperties.getHost(), bcsProperties.getPort()));
            var request = RequestEntity
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(purchase.marshalJson());
            return client.exchange(request, String.class);

        } catch (HttpClientErrorException e) {
            return ResponseEntity
                .status(e.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(e.getResponseBodyAsString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(StringUtil.singleEntryJson(
                    "message", "ERROR: Internal Server Error"));
        } catch (RestClientException e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(StringUtil.singleEntryJson(
                    "message", "ERROR: External API Error"));
        }
    }
}
