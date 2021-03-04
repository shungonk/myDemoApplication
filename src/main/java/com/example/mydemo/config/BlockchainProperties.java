package com.example.mydemo.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "blockchain")
public class BlockchainProperties {
    
    private String uri;

    public URI getUri() throws URISyntaxException {
        return new URI(uri);
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
