package com.example.mydemo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "blockchain")
public class BlockchainProperties {
    
    private String scheme;
    private String url;

    public String getScheme() {
        return scheme;
    }

    public String getUrl() {
        return url;
    }

    public String getAbsoluteUri() {
        return String.format("%s://%s", getScheme(), getUrl());
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
