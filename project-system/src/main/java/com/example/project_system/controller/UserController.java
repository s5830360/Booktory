package com.example.project_system.controller;

import com.example.project_system.entity.User;
import com.example.project_system.repository.ReviewRepository;
import com.example.project_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    // 사용자 정보 + 서평 통계 조회
    @GetMapping("/{username}")
    public ResponseEntity<?> getUserInfo(@PathVariable String username) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    int reviewCount = reviewRepository.countByUser_Username(username);
                    Double avgRating = reviewRepository.getAverageRatingByUsername(username);

                    Map<String, Object> result = new HashMap<>();
                    result.put("username", user.getUsername());
                    result.put("joinedDate", user.getCreatedAt());
                    result.put("reviewCount", reviewCount);
                    result.put("averageRating", avgRating != null ? avgRating : 0.0);

                    return ResponseEntity.ok(result);
                })
                .orElse(ResponseEntity.status(404).body(Map.of("error", "사용자 없음")));
    }

    @PatchMapping("/{username}/nickname")
    public ResponseEntity<?> updateNickname(@PathVariable String username, @RequestBody Map<String, String> body) {
        String newNickname = body.get("nickname");
        return userRepository.findByUsername(username)
                .map(user -> {
                    user.setNickname(newNickname);
                    userRepository.save(user);
                    return ResponseEntity.ok(Map.of("message", "닉네임 수정 완료"));
                })
                .orElse(ResponseEntity.status(404).body(Map.of("error", "사용자 없음")));
    }

}
