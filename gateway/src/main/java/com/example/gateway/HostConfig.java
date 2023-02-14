package com.example.gateway;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "microservices")
@ConfigurationPropertiesScan
public class HostConfig {

    private String quoteServiceHost;
    private String userServiceHost;
    private String voteServiceHost;

}
