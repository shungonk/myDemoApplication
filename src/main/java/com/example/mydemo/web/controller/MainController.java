package com.example.mydemo.web.controller;

import java.net.URI;
import java.net.URISyntaxException;

import com.example.mydemo.util.StringUtil;
import com.example.mydemo.web.model.TransactionForm;
import com.example.mydemo.web.model.TransactionRequest;
import com.example.mydemo.web.service.WalletService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class MainController {

    @Value("${blockchain.host}")
    private String bchost;

    @Value("${blockchain.port}")
    private int bcport;

    @Autowired
    private WalletService walletService;
    
    @GetMapping(value="/home")
    public String home(Authentication authentication, Model model) {
        var user = (User) authentication.getPrincipal();
        var walletList = walletService.findByUsername(user.getUsername());
        model.addAttribute("walletList", walletList);
        return "home";
    }

    @GetMapping(value="/send")
    public String send(@RequestParam("name") String name, Authentication authentication, Model model) {
        var user = (User) authentication.getPrincipal();
        var wallet = walletService.findByPrimaryKey(name, user.getUsername());
        model.addAttribute("wallet", wallet);
        return "send";
    }

    @ResponseBody
    @PostMapping(value="/transaction", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendTransaction(@Validated @RequestBody TransactionForm form) {
		var request = new TransactionRequest(
			form.getSenderPublicKey(), 
			form.getSenderAddress(),
			form.getRecipientAddress(),
			form.getValue(),
			form.generateSignature());

        if (!request.validateTransactionRequest() ||
            !request.verifySignature()) {
            return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(StringUtil.messageJson("Invalid transaction"));
        }

        try {
            var uri = new URI("http://" + bchost + ":" + bcport + "/transaction");
            var client = new RestTemplate(new SimpleClientHttpRequestFactory());
            var req = RequestEntity
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request.marshalJson());
            return client.exchange(req, String.class);

        } catch (URISyntaxException e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Server error");
        }
    }
}
