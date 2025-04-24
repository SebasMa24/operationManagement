package com.quantumdev.integraservicios.operationManagement.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1/operation")
public class DemoController {
    @PostMapping("/public")
    public String welcome1()
    {
        return "Welcome to a public endpoint at operationManagement microservice";
    }
    @PostMapping("/demo")
    public String welcome()
    {
        return "Welcome from secure endpoint at operationManagement microservice";
    }
}
