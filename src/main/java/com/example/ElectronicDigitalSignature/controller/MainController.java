package com.example.ElectronicDigitalSignature.controller;

import com.example.ElectronicDigitalSignature.entity.UserEntity;
import com.example.ElectronicDigitalSignature.service.IKeyService;
import com.example.ElectronicDigitalSignature.service.ITokenService;
import com.example.ElectronicDigitalSignature.service.IUserService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@AllArgsConstructor
public class MainController {

    private ITokenService tokenService;
    private IKeyService keyService;
    private final IUserService userService;

    @GetMapping("/")
    public String index(Authentication authentication, Model model) {
        if (authentication != null) {
            UserEntity user = (UserEntity) authentication.getPrincipal();
            model.addAttribute("user", user);
        }
        return "index";
    }

    @GetMapping("/contact-us")
    public String contactUs(Authentication authentication, Model model) {
        if (authentication != null) {
            UserEntity user = (UserEntity) authentication.getPrincipal();
            model.addAttribute("user", user);
        }
        model.addAttribute("managers", userService.getManagers());
        return "contact-us";
    }

    @GetMapping("/about-us")
    public String aboutUs(Authentication authentication, Model model) {
        if (authentication != null) {
            UserEntity user = (UserEntity) authentication.getPrincipal();
            model.addAttribute("user", user);
        }
        return "about-us";
    }

    @GetMapping("/manager")
    public String manager(Authentication authentication, Model model) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "manager";
    }

    @GetMapping("/download-key")
    public String downloadKey() {
        return "download-key";
    }

    @GetMapping("/download-key/")
    public void downloadFile(@RequestParam("filename") String code,
                             String email,
                             Model model,
                             HttpServletResponse response) throws IOException {
        UserEntity user = userService.getByEmail(email);
        if (user == null) {
            return;
        }
        boolean isExists = tokenService.existsTokenByEmail(user, code);
        if (!isExists) {
            return;
        }
//        KeyEntity keyEntity = keyService.getByUser(user);
//
        Path myPath = Paths.get( "keys");
//        FileWriter myWriter = new FileWriter(myPath + "/" + user.getId() + "filename.txt");
//
//        myWriter.write(keyEntity.getPublicKey());
//        myWriter.close();

        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = key";
        response.setHeader(headerKey, headerValue);
        ServletOutputStream outputStream = response.getOutputStream();

        File file = new File( myPath + "/" + user.getId() +  "/private.key");
        byte[] bytes = new byte[(int) file.length()];

        FileInputStream fis = new FileInputStream(file);

        outputStream.write(fis.readAllBytes());
        outputStream.close();
    }

    @GetMapping("/test")
    public String test(Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        keyService.test(user);
        return "about-us";
    }

    @GetMapping("/download-keys")
    public void downloadKeys(@RequestParam("type") String type,
                             Authentication authentication,
                             HttpServletResponse response) throws IOException {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        Path myPath = Paths.get( "keys");
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = key";
        if (type.equals("private")) {
            headerValue = "attachment; filename = private.key";
        } else if (type.equals("public")){
            headerValue = "attachment; filename = public.key";
        }

        response.setHeader(headerKey, headerValue);
        ServletOutputStream outputStream = response.getOutputStream();

        File file = null;
        if (type.equals("private")) {
            file = new File( myPath + "/" + user.getId() +  "/private.key");
        } else if (type.equals("public")){
            file = new File( myPath + "/" + user.getId() +  "/public.key");
        }

        byte[] bytes = new byte[(int) file.length()];

        FileInputStream fis = new FileInputStream(file);

        outputStream.write(fis.readAllBytes());
        outputStream.close();
    }
}
