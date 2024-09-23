package com.kelvin.login_auth_api.service;

import com.kelvin.login_auth_api.config.security.TokenService;
import com.kelvin.login_auth_api.dto.LoginRequestDTO;
import com.kelvin.login_auth_api.dto.RegisterRequestDTO;
import com.kelvin.login_auth_api.dto.ResponseDTO;
import com.kelvin.login_auth_api.repositories.UserRepository;
import com.kelvin.login_auth_api.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public ResponseEntity login(@RequestBody LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.email()).orElseThrow(() -> new RuntimeException("Email not found"));
        if(passwordEncoder.matches(loginRequestDTO.password(), user.getPassword())) {
            String token = tokenService.generateToken(user);
            return ResponseEntity.ok(new ResponseDTO(user.getId(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity register(RegisterRequestDTO registerRequestDTO) {
        Optional user = userRepository.findByEmail(registerRequestDTO.email());
        if(user.isEmpty()) {
            User newUser = new User();
            newUser.setPassword(passwordEncoder.encode(registerRequestDTO.password()));
            newUser.setEmail(registerRequestDTO.email());
            newUser.setName(registerRequestDTO.name());
            this.userRepository.save(newUser);

            String token = tokenService.generateToken(newUser);
            return ResponseEntity.ok(new ResponseDTO(newUser.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }
}
