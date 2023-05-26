package com.example.ElectronicDigitalSignature.controller;

import com.example.ElectronicDigitalSignature.entity.UserEntity;
import com.example.ElectronicDigitalSignature.service.IKeyService;
import com.example.ElectronicDigitalSignature.service.IOtinishService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@AllArgsConstructor
public class RequestController {

    private final IOtinishService otinishService;
    private final IKeyService keyService;

    @GetMapping("/otinishter")
    public String otinishter(Authentication authentication, Model model) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("otinishter", otinishService.getByUser(user));
        return "otinishter";
    }

    @GetMapping("/manager-otinishter")
    public String managerOtinishter(Authentication authentication, Model model) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("otinishter", otinishService.getAll());
        return "manager-otinish";
    }

    @GetMapping("/otinish")
    public String otinish(Authentication authentication, Model model,
                          @RequestParam("id") Long id) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("otinish", otinishService.getById(id));
        return "otinish";
    }

    @GetMapping("/new-otinish")
    public String aboutUs(Authentication authentication, Model model) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "new-otinish";
    }

    @PostMapping("/new-request")
    public String createRequest(Authentication authentication, String type, String organ, String description, String address, MultipartFile key) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        boolean isUserKey = keyService.verifyKey(user, key);
        if (isUserKey) {
            otinishService.create(user, type, organ, description, address);
        } else {
            return "redirect:/new-otinish?error";
        }

        return "redirect:/otinishter?success";
    }

}
