package com.example.mydemo.web.controller;

import java.net.URI;
import java.net.URISyntaxException;

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
            var res = balance(wallet.getAddress());
            if (res.getStatusCodeValue() == 200) {
                var balance = StringUtil.valueInJson(res.getBody(), "balance");
                wallet.setBalance(Float.parseFloat(balance));
            }
        }
        model.addAttribute("walletList", walletList);
        return "home";
    }

    @GetMapping(value="/wallet")
    public String wallet(@RequestParam("name") String name, Authentication auth, Model model) {
        var user = (User) auth.getPrincipal();
        var wallet = walletService.findByNameAndUsername(name, user.getUsername());
        model.addAttribute("wallet", wallet);
        return "wallet";
    }

    @ResponseBody
    @PostMapping(value="/wallet/new", produces = "application/json")
    public String walletNew(@RequestParam("name") String name, Authentication auth, Model model) {
        var user = (User) auth.getPrincipal();
        var count = walletService.countByNameAndUsername(name, user.getUsername());
        if (count != 0) {
            return StringUtil.messageJson("Error: Name Already Exists.");
        }
        var newWallet = Wallet.create(name);

        // purchase
        var value = 1000f;
        var res = purchase(new PurchaseRequest(
            newWallet.getPublicKey(), 
            newWallet.getAddress(), 
            value,
            SecurityUtil.createEcdsaSign(newWallet.getPrivateKey(), newWallet.getAddress() + Float.toString(value))));
        if (res.getStatusCodeValue() != 200) {
            return StringUtil.messageJson("Error: Create failed.");
        }
        
        walletService.save(user.getUsername(), newWallet);
        return StringUtil.messageJson("Created!");
    }

    @ResponseBody
    @GetMapping(value="/balance")
    public ResponseEntity<String> balance(@RequestParam("address") String address) {
        try {
            var client = new RestTemplate(new SimpleClientHttpRequestFactory());
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            var uri = new URI(String.format("http://%s:%s/balance",
                bcsProperties.getHost(), bcsProperties.getPort()));
            var queryURI = UriComponentsBuilder
                .fromUri(uri)
                .queryParam("address", address)
                .build().encode().toUri();
            var req = new RequestEntity<>(headers, HttpMethod.GET, queryURI);
            return client.exchange(req, String.class);

        } catch (URISyntaxException e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Server Error");
        } catch (RestClientException e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body("External API Error");
        }
    }

    @ResponseBody
    @PostMapping(value="/transaction", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendTransaction(@RequestBody TransactionForm form) {
		var transaction = new TransactionRequest(
			form.getSenderPublicKey(), 
			form.getSenderAddress(),
			form.getRecipientAddress(),
			form.getValue(),
			form.generateSignature());

        try {
            var client = new RestTemplate(new SimpleClientHttpRequestFactory());
            var uri = new URI(String.format("http://%s:%s/transaction",
                bcsProperties.getHost(), bcsProperties.getPort()));
            var request = RequestEntity
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(transaction.marshalJson());
            return client.exchange(request, String.class);

        } catch (URISyntaxException e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Server Error");
        } catch (RestClientException e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body("External API Error");
        }
    }

    ////////// for demo //////////
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

        } catch (URISyntaxException e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Server Error");
        } catch (RestClientException e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body("External API Error");
        }
    }
    //////////////////////////////

    public ResponseEntity<String> purchase(PurchaseRequest purchase) {
        try {
            var client = new RestTemplate(new SimpleClientHttpRequestFactory());
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            var uri = new URI(String.format("http://%s:%s/purchase",
                bcsProperties.getHost(), bcsProperties.getPort()));
            var request = RequestEntity
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(purchase.marshalJson());
            return client.exchange(request, String.class);

        } catch (URISyntaxException e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Server Error");
        } catch (RestClientException e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body("External API Error");
        }
    }
}
