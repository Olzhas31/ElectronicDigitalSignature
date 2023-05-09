package com.example.ElectronicDigitalSignature.controller;

import com.example.ElectronicDigitalSignature.entity.MessageEntity;
import com.example.ElectronicDigitalSignature.entity.UserEntity;
import com.example.ElectronicDigitalSignature.service.IMessageService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@AllArgsConstructor
public class EmailController {

    private final IMessageService messageService;

    @GetMapping("/inbox")
    public String inbox(Authentication authentication, Model model) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        List<MessageEntity> sent = messageService.getAllBySender(user);
        List<MessageEntity> inbox = messageService.getAllByReceiver(user);
        int inboxSize = 0;
        for (MessageEntity message : inbox) {
            if (!message.getIsRead()) {
                inboxSize++;
            }
        }
        model.addAttribute("inbox", inbox);
        model.addAttribute("inboxSize", inboxSize);
        model.addAttribute("sent", sent);
        model.addAttribute("user", user);
        return "inbox";
    }

    @GetMapping("/sent")
    public String sent(Authentication authentication, Model model) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        List<MessageEntity> sent = messageService.getAllBySender(user);
        List<MessageEntity> inbox = messageService.getAllByReceiver(user);
        int inboxSize = 0;
        for (MessageEntity message : inbox) {
            if (!message.getIsRead()) {
                inboxSize++;
            }
        }

        model.addAttribute("inboxSize", inboxSize);
        model.addAttribute("sent", sent);
        model.addAttribute("user", user);
        return "sent";
    }

    @GetMapping("/message")
    public String message(Authentication authentication, Model model,
                          @RequestParam("id") Long id) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        MessageEntity message = messageService.getById(id, user);
        List<MessageEntity> sent = messageService.getAllBySender(user);
        List<MessageEntity> inbox = messageService.getAllByReceiver(user);
        int inboxSize = 0;
        for (MessageEntity messageEntity : inbox) {
            if (!messageEntity.getIsRead()) {
                inboxSize++;
            }
        }

        model.addAttribute("isSign", messageService.isSign(id, message.getSender().getId()));
        model.addAttribute("sent", sent);
        model.addAttribute("inboxSize", inboxSize);
        model.addAttribute("message", message);
        model.addAttribute("user", user);
        return "message";
    }

    @GetMapping("/send-message")
    public String showSendMessage(Authentication authentication, Model model) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        List<MessageEntity> sent = messageService.getAllBySender(user);
        List<MessageEntity> inbox = messageService.getAllByReceiver(user);
        int inboxSize = 0;
        for (MessageEntity messageEntity : inbox) {
            if (!messageEntity.getIsRead()) {
                inboxSize++;
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("sent", sent);
        model.addAttribute("inboxSize", inboxSize);
        return "send-message";
    }
    
    @PostMapping("/send-message")
    public String sendMessage(Authentication authentication, String title, String email, String content, MultipartFile key) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        Long id = messageService.send(user, email, title, content, key);
        return "redirect:/message?id=" + id;
    }
}
