package com.example.gateway;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@AllArgsConstructor
public class Config {

    private HostConfig hostConfig;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/user/**")
                        .uri("http://" + hostConfig.getUserServiceHost() + ":8081"))
                .route(p -> p
                        .path("/quote/**")
                        .uri("http://" + hostConfig.getQuoteServiceHost() + ":8082"))
                .route(p -> p
                        .path("/vote/**")
                        .uri("http://" + hostConfig.getVoteServiceHost() + ":8083"))
                .build();
    }

}
