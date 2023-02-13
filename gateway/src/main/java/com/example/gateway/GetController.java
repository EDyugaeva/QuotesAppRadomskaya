package com.example.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/get")
public class GetController {

    @GetMapping
    public String get() {
        return "Hello from Gateway!";
    }

}
