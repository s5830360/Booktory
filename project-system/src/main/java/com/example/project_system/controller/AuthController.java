package com.example.project_system.controller;

import com.example.project_system.entity.User;
import com.example.project_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.Map;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser, HttpSession session) {
        return userRepository.findByUsername(loginUser.getUsername())
                .map(user -> {
                    if (user.getPassword().equals(loginUser.getPassword())) {
                        return ResponseEntity.ok().body(Map.of(
                                "message", "로그인 성공",
                                "username", user.getUsername(),
                                "nickname", user.getNickname()
                        ));
                    } else {
                        return ResponseEntity
                                .status(401)
                                .body(Map.of("error", "비밀번호 틀림"));
                    }
                })
                .orElse(ResponseEntity
                        .status(404)
                        .body(Map.of("error", "사용자 없음")));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User newUser) {
        Optional<User> existing = userRepository.findByUsername(newUser.getUsername());
        if (existing.isPresent()) {
            return ResponseEntity.status(400).body("이미 존재하는 사용자입니다.");
        }

        // 평문 비밀번호 저장 (나중에 BCrypt로 암호화 권장)
        User user = User.builder()
                .username(newUser.getUsername())
                .password(newUser.getPassword())
                .email(newUser.getEmail())  // email 필드가 있다면!
                .nickname(newUser.getUsername())
                .build();

        userRepository.save(user);
        return ResponseEntity.ok("회원가입 완료!");
    }

}