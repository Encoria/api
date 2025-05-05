package com.encoria.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MockController {

    @GetMapping("/api/public/hello")
    public String hello() {
        return "hello";
    }
}
