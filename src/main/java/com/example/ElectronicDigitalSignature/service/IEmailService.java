package com.example.ElectronicDigitalSignature.service;

public interface IEmailService {
    void sendEmail(String password, String email, String code);
}
