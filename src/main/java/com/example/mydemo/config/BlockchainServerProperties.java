package com.example.mydemo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "blockchain")
public class BlockchainServerProperties {
    
    private String hostUri;

    public String getHostUri() {
        return hostUri;
    }

    public void setHost(String hostUri) {
        this.hostUri = hostUri;
    }
}
