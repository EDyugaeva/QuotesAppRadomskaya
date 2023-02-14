package com.example.gateway;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "/get")
public class GetController {

    HostConfig hostConfig;

    @GetMapping
    public String get() {
        log.info(hostConfig.getQuoteServiceHost());
        return "Hello from Gateway!";
    }

}
