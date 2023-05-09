package com.example.ElectronicDigitalSignature.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class RequestController {

    @GetMapping("/requests")
    public String aboutUs() {
        return "requests";
    }

}
