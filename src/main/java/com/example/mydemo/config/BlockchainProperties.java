package com.example.mydemo.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "blockchain")
public class BlockchainProperties {
    
    private String url;

    public URI getUrl() throws URISyntaxException {
        return new URI(url);
    }

    public void setUri(String url) {
        this.url = url;
    }
}
