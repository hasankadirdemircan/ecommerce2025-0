package com.lala.ecommerce.service;

import com.lala.ecommerce.dto.LoginDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String extractEmail(String token);
    boolean validateToken(String token, UserDetails userDetails);
    LoginDto generateToken(Authentication authentication);

}
