package com.example.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/user/**")
                        .uri("http://localhost:8081/"))
                .route(p -> p
                        .path("/quote/**")
                        .uri("http://localhost:8082"))
                .route(p -> p
                        .path("/vote/**")
                        .uri("http://localhost:8083"))
                .build();
    }

}
