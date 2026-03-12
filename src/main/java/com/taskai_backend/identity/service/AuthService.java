package com.taskai_backend.identity.service;


import com.taskai_backend.identity.entity.User;
import com.taskai_backend.identity.repository.UserRepository;
import com.taskai_backend.common.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public void register(String email, String password, String name) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("User already exists");
        }
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .fullName(name)
                .subscriptionStatus(User.SubscriptionStatus.FREE)
                .build();
        userRepository.save(user);
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return jwtUtils.generateToken(user.getEmail());
    }
}
