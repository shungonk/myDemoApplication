package com.example.mydemo.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "blockchain")
public class BlockchainProperties {
    
    private String uri;

    public URI getUrl() throws URISyntaxException {
        return new URI(uri);
    }

    public void setUri(String uri) {
        // local
        this.uri = String.format("http://%s", uri);
        // Heroku
        // this.uri = String.format("https://%s", System.getenv("BLOCKCHAIN_URL"));
    }
    
}
