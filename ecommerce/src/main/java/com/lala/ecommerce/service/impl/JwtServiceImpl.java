package com.lala.ecommerce.service.impl;

import com.lala.ecommerce.dto.LoginDto;
import com.lala.ecommerce.model.Customer;
import com.lala.ecommerce.repository.CustomerRepository;
import com.lala.ecommerce.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    public static final String SECRET = "404D635166546A576E5A7234753778214125442A472D4B6150645267556B5870";

    private final CustomerRepository customerRepository;

    @Override
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    };

    @Override
    public LoginDto generateToken(Authentication authentication) {
        LoginDto loginDto = new LoginDto();
        String authenticationName = authentication.getName();
        Optional<Customer> customerOptional = customerRepository.findByEmail(authenticationName);

        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", authentication.getAuthorities());
        claims.put("name", authenticationName);
        if(customerOptional.isPresent()) {
            loginDto.setCustomerId(customerOptional.get().getId());
        }
        loginDto.setToken(createToken(claims, authenticationName));

        return loginDto;
    }

    private String createToken(Map<String, Object> claims, String authenticationName) {
        return Jwts.builder()
                .claims(claims)
                .subject(authenticationName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 100 * 60 * 100))
                .signWith(getSignKey())
                .compact();
    }
}
