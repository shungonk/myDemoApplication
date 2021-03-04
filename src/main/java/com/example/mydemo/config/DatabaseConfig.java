package com.example.mydemo.config;

import org.springframework.context.annotation.Configuration;

// Heroku
@Configuration
public class DatabaseConfig {

    // @Value("${spring.datasource.url}")
    // private String dbUrl;

    // @Bean
    // public DataSource dataSource() {
    //     HikariConfig config = new HikariConfig();
    //     config.setJdbcUrl(dbUrl);
    //     return new HikariDataSource(config);
    // }
}