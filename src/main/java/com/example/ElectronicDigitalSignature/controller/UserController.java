package com.example.ElectronicDigitalSignature.controller;

import com.example.ElectronicDigitalSignature.entity.UserEntity;
import com.example.ElectronicDigitalSignature.service.IUserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping("/user-info")
    public String userInfo(Authentication authentication, Model model) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        System.out.println(user);
        model.addAttribute("user", user);
        return "user-info";
    }

    @GetMapping("/register-user")
    public String showRegisterUser(Authentication authentication, Model model) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "register-user";
    }

    @PostMapping("/register-user")
    public String registerUser(String iin, String name, String surname,
                               String midName, String email, String gender, String phoneNumber) {
        userService.create(iin, name, surname, midName, email, gender, phoneNumber);
        return "redirect:/";
    }

    @PostMapping("/user-edit")
    public String editUser(Authentication authentication,
                           String name, String surname, String midName, String password) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        userService.update(user.getId() , name, surname, midName, password);
        return "redirect:/logout";
    }
}
