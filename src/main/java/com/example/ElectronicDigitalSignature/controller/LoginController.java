package com.example.ElectronicDigitalSignature.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/forget-pass")
    public String forgetPass() {
        return "forget-pass";
    }

}
