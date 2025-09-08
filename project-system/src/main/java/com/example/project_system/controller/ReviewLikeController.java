package com.example.project_system.controller;

import com.example.project_system.entity.Review;
import com.example.project_system.entity.ReviewLike;
import com.example.project_system.entity.User;
import com.example.project_system.repository.ReviewLikeRepository;
import com.example.project_system.repository.ReviewRepository;
import com.example.project_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review-likes")
public class ReviewLikeController {

    private final ReviewLikeRepository likeRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    // ❤️ 좋아요 토글
    @PostMapping
    public ResponseEntity<String> toggleLike(@RequestParam Long reviewId, @RequestParam String username) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<ReviewLike> existing = likeRepository.findByReviewAndUser(review, user);
        if (existing.isPresent()) {
            likeRepository.delete(existing.get());
            return ResponseEntity.ok("unliked");
        } else {
            ReviewLike like = ReviewLike.builder()
                    .review(review)
                    .user(user)
                    .build();
            likeRepository.save(like);
            return ResponseEntity.ok("liked");
        }
    }

    // 🔢 좋아요 수 조회
    @GetMapping("/count")
    public ResponseEntity<Long> getLikeCount(@RequestParam Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        long count = likeRepository.countByReview(review);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/is-liked")
    public ResponseEntity<Boolean> isLiked(
            @RequestParam Long reviewId,
            @RequestParam String username
    ) {
        Optional<Review> reviewOpt = reviewRepository.findById(reviewId);
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (reviewOpt.isEmpty() || userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(false);
        }

        boolean liked = likeRepository.findByReviewAndUser(reviewOpt.get(), userOpt.get()).isPresent();
        return ResponseEntity.ok(liked);
    }

}
