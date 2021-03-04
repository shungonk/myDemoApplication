package com.example.mydemo.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "blockchain")
public class BlockchainProperties {
    
    private String host;
    private int port;
    private URI uri;

    public URI getUrl() {
        return uri;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void setUri(URI uri) throws URISyntaxException {
        // local
        // this.uri = new URI(String.format("http://%s:%s", getHost(), getPort()));
        // Heroku
        this.uri = new URI(String.format("https://%s", System.getenv("BLOCKCHAIN_URL")));
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
}
