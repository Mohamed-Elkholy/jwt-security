package com.kholy.bsmart.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kholy.bsmart.dto.AuthRequest;
import com.kholy.bsmart.dto.AuthResponse;
import com.kholy.bsmart.dto.RegisterRequest;
import com.kholy.bsmart.entity.Token;
import com.kholy.bsmart.entity.User;
import com.kholy.bsmart.enums.TokenType;
import com.kholy.bsmart.repository.TokenRepo;
import com.kholy.bsmart.repository.UserRepo;
import com.kholy.bsmart.security.JwtServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final TokenRepo tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtServices jwtServices;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest registerRequest) {
        var user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();
        if (registerRequest.getFirstName().equals("admin") && registerRequest.getLastName().equals("admin") ) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.CUSTOMER);
        }
        var savedUser = userRepo.save(user);
        var jwtToken = jwtServices.generateToken(user);
        var refreshToken = jwtServices.generateRefreshToken(user);
        var authResponse = AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .message("register success :)")
                .build();
        saveToken(jwtToken, savedUser);
        return authResponse;
    }

    public AuthResponse login(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );
        var user = userRepo.findByEmail(authRequest.getEmail()).orElseThrow();
        var jwtToken = jwtServices.generateToken(user);
        var refreshToken = jwtServices.generateRefreshToken(user);
        var authResponse = AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .message("login success :)")
                .build();
        revokeAllUserTokens(user);
        saveToken(jwtToken, user);
        return authResponse;
    }

    private void saveToken(String jwtToken, User user) {
        var token = Token.builder()
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .user(user)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepo.save(token);
    }
    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepo.findAllValidTokensByUser(user.getId());
        if(validUserTokens.isEmpty()) {
            return ;
        }
        validUserTokens.forEach(t -> {
            t.setRevoked(true);
            t.setExpired(true);
        });
        tokenRepo.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtServices.extractUserName(refreshToken);
        if (userEmail != null) {
            var user = this.userRepo.findByEmail(userEmail).orElseThrow();
            if (jwtServices.isTokenValid(refreshToken, user)) {
                var accessToken = jwtServices.generateToken(user);
                revokeAllUserTokens(user);
                saveToken(accessToken, user);
                var authResponse = AuthResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
