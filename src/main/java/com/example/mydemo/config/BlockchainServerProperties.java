package com.example.mydemo.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "blockchain")
public class BlockchainServerProperties {
    
    private String host;
    private int port;

    public URI getUrl() throws URISyntaxException {
        // local
        return new URI(String.format("http://%s:%s", getHost(), getPort()));
        // Heroku
        // return new URI(String.format("https://%s", System.getenv("BLOCKCHAIN_URL")));
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
}
