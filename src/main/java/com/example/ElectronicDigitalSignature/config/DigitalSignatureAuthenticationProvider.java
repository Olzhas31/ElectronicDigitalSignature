package com.example.ElectronicDigitalSignature.config;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;

public class DigitalSignatureAuthenticationProvider implements AuthenticationProvider {

//    private DigitalSignatureService digitalSignatureService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String password = (String) authentication.getCredentials();
        var file = (Object) authentication.getPrincipal();
        System.out.println(file);
        byte[] signatureBytes = Base64.getDecoder().decode(password);

        boolean signatureValid;
        try {
//            signatureValid = digitalSignatureService.verifySignature(authentication.getPrincipal().toString().getBytes(), signatureBytes);
            signatureValid = password.equals("test");
            System.out.println("signatureValid: " + signatureValid);
        } catch (Exception e) {
            throw new AuthenticationServiceException("Error verifying digital signature", e);
        }

        if (signatureValid) {
//            UserDetails userDetails = new CustomUserDetailsService().loadUserByUsername(authentication.getPrincipal().toString());
//            AbstractAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
            return authentication;
        } else {
            throw new BadCredentialsException("Invalid digital signature");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
