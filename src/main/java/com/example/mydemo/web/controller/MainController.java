package com.example.mydemo.web.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;

import com.example.mydemo.config.BlockchainProperties;
import com.example.mydemo.domain.service.UserService;
import com.example.mydemo.domain.service.WalletService;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class MainController {

    @Autowired
    BlockchainProperties bcsProps;

    @Autowired
    WalletService walletService;

    @Autowired
    UserService userService;

    @GetMapping(value="/home")
    public String home(Authentication auth, Model model) {
        var user = (User) auth.getPrincipal();
        var walletList = walletService.findByUsername(user.getUsername());
        for (var wallet: walletList) {
            var res = requestBalance(wallet.getAddress());
            var balanceStr = StringUtil.valueInJson(res.getBody(), "balance");
            wallet.setBalanceStr(balanceStr);
        }
        model.addAttribute("walletList", walletList);
        model.addAttribute("isActive", userService.isUserActive(user.getUsername()));
        return "home";
    }
    
    @GetMapping(value="/wallet")
    public String wallet(@RequestParam("name") String name, Authentication auth, Model model) {
        var user = (User) auth.getPrincipal();
        var wallet = walletService.findByNameAndUsername(name, user.getUsername());         
        // get balance   
        var rspBalance = requestBalance(wallet.getAddress());
        var balanceStr = StringUtil.valueInJson(rspBalance.getBody(), "balance");
        wallet.setBalanceStr(balanceStr);
        // get information of blockchain
        var rspInfo = requestInfo();
        model.addAttribute("info", StringUtil.formatJson(rspInfo.getBody()));
        model.addAttribute("wallet", wallet);
        model.addAttribute("isActive", userService.isUserActive(user.getUsername()));
        return "wallet";
    }

    @ResponseBody
    @PostMapping(value="/wallet/new", produces = MediaType.APPLICATION_JSON_VALUE)
    public String createWallet(@RequestParam("name") String name, Authentication auth, Model model) {
        var user = (User) auth.getPrincipal();
        if (!userService.isUserActive(user.getUsername())) {
            return StringUtil.makeJson("message", "FAILED: User Not Active.");
        }
        var count = walletService.countByNameAndUsername(name, user.getUsername());
        if (count != 0) {
            return StringUtil.makeJson("message", "FAILED: Same Name Already Exists.");
        }
        var newWallet = Wallet.create(name);

        // create purchase request
        var purchaseReq = new PurchaseRequest(
            newWallet.getAddress(), 
            new BigDecimal("30"), 
            Instant.now().toEpochMilli());
        purchaseReq.signate(newWallet.getPrivateKey(), newWallet.getPublicKey());

        var res = requestPurchase(purchaseReq);
        if (res.getStatusCode().isError()) {
            return StringUtil.makeJson("message", "FAILED: Failed to create");
        } 
        walletService.save(user.getUsername(), newWallet);
        return StringUtil.makeJson("message", "SUCCESS: Created!");
    }

    @ResponseBody
    @GetMapping(value="/balance", produces = MediaType.APPLICATION_JSON_VALUE)
    public String balance(@RequestParam("address") String address) {
        var res = requestBalance(address);
        return res.getBody();
    }

    @ResponseBody
    @PostMapping(value="/transaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public String transaction(@RequestBody TransactionForm form, Authentication auth) {
        var user = (User) auth.getPrincipal();
        if (!userService.isUserActive(user.getUsername())) {
            return StringUtil.makeJson("message", "FAILED: User Not Active.");
        }
        // create transaction request
		var transactionReq = new TransactionRequest(
			form.getSenderAddress(),
			form.getRecipientAddress(),
			form.getAmount(),
			Instant.now().toEpochMilli());
		transactionReq.signate(form.getSenderPrivateKey(), form.getSenderPublicKey());

        if (!transactionReq.validateFields())
            return StringUtil.makeJson("message", "FAILED: Fill in the all blanks");
        if (!transactionReq.validateAmount())
            return StringUtil.makeJson("message", "FAILED: Amount should be positive");
        if (transactionReq.getSenderAddress().equals(transactionReq.getRecipientAddress()))
            return StringUtil.makeJson("message", "FAILED: Address cannot be same as sender's one");

        var res = requestTransaction(transactionReq);
        return res.getBody();
    }

    @ResponseBody
    @GetMapping(value="/info")
    public String info() {
        var res = requestInfo();
        var printInfo = StringUtil.formatJson(res.getBody());
        return printInfo;
    }

    public ResponseEntity<String> requestBalance(String address) {
        try {
            var client = new RestTemplate(new SimpleClientHttpRequestFactory());
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            var uri = UriComponentsBuilder
                .fromUri(new URI(bscProps.getAbsoluteUri()))
                .path("/balance")
                .queryParam("address", address)
                .build().encode().toUri();
            var req = new RequestEntity<>(headers, HttpMethod.GET, uri);
            return client.exchange(req, String.class);

        } catch (HttpClientErrorException e) {
            return ResponseEntity
                .status(e.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(StringUtil.makeJson(
                    "message", "ERROR: Server Error",
                    "balance", "0.000000"));
        }
    }

    public ResponseEntity<String> requestTransaction(TransactionRequest transactionReq) {
        try {
            var client = new RestTemplate(new SimpleClientHttpRequestFactory());
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            var uri = UriComponentsBuilder
                .fromUri(new URI(bscProps.getAbsoluteUri()))
                .path("/transaction")
                .build().encode().toUri();
            var req = new RequestEntity<>(transactionReq.toJson(), headers, HttpMethod.POST, uri);
            return client.exchange(req, String.class);

        } catch (HttpClientErrorException e) {
            return ResponseEntity
                .status(e.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(StringUtil.makeJson(
                    "message", "ERROR: Server Error"));
        }
    }

    public ResponseEntity<String> requestPurchase(PurchaseRequest purchaseReq) {
        try {
            var client = new RestTemplate(new SimpleClientHttpRequestFactory());
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            var uri = UriComponentsBuilder
                .fromUri(new URI(bscProps.getAbsoluteUri()))
                .path("/purchase")
                .build().encode().toUri();
            var req = new RequestEntity<>(purchaseReq.toJson(), headers, HttpMethod.POST, uri);
            return client.exchange(req, String.class);

        } catch (HttpClientErrorException e) {
            return ResponseEntity
                .status(e.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(StringUtil.makeJson(
                    "message", "ERROR: Server Error"));
        }
    }

    public ResponseEntity<String> requestInfo() {
        try {
            var client = new RestTemplate(new SimpleClientHttpRequestFactory());
            var uri = UriComponentsBuilder
                .fromUri(new URI(bscProps.getAbsoluteUri()))
                .path("/info")
                .build().encode().toUri();
            var req = new RequestEntity<>(HttpMethod.GET, uri);
            return client.exchange(req, String.class);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(StringUtil.makeJson(
                    "ERROR", "Server Error"));
        }
    }
}
