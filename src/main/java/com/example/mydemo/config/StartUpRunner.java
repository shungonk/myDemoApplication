package com.example.mydemo.config;

import javax.annotation.PostConstruct;

import com.example.mydemo.domain.service.UserService;
import com.example.mydemo.domain.service.WalletService;
import com.example.mydemo.web.model.Wallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartUpRunner {

    @Autowired
    UserService userService;

    @Autowired
    WalletService walletService;

    @PostConstruct
    public void init(){
		// for demo
        var minerName = "TestMiner";
        var minerPass = "TestMiner";
        var walletName = "Unbelievable_Wallet";
        if (!userService.isUsernameExists(minerName))
            userService.registerInactiveUser(minerName, minerPass);
        if (walletService.countByNameAndUsername(walletName, minerName) == 0)
    		walletService.save("TestMiner", new Wallet(
                "Unbelievable_Wallet",
                "1K8if6hiWRapKLxWAdVcpys2QbReBD5P6p", 
                "MD4CAQAwEAYHKoZIzj0CAQYFK4EEAAoEJzAlAgEBBCAFzE1GvP3x7LWRqjHLVrTKdhfLrHinB0a8zL0FMnRnSw==", 
                "MFYwEAYHKoZIzj0CAQYFK4EEAAoDQgAEeJ9hfzw+tkrBV47QM67htmK31Vg/otuDudsMFIRpVKWsqr+1kJsnifB4gpjWTIyc4UJIdaQsAJ000q+TFfPJlQ=="));
    }
}