package com.example.project_system.controller;

import com.example.project_system.dto.ReviewDto;
import com.example.project_system.entity.User;
import com.example.project_system.repository.UserRepository;
import com.example.project_system.repository.ReviewRepository;
import com.example.project_system.service.ReviewService;
import com.example.project_system.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;
import com.example.project_system.dto.ReviewStatsDto;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    // 최신 서평 5개 반환
    @GetMapping("/recent")
    public List<ReviewDto> getRecentReviews() {
        List<Review> reviews = reviewService.getRecentReviews();
        return reviews.stream()
                .filter(r -> r.getContent() != null)  // optional
                .map(ReviewDto::fromEntity)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ReviewDto createReview(@RequestBody ReviewDto dto) {
        System.out.println("DTO로 넘어온 username = " + dto.getUsername());
        Optional<User> userOpt = userRepository.findByUsername(dto.getUsername());
        System.out.println("DB 조회 결과 = " + userOpt);

        // username으로 User 찾기
        User user = userOpt.orElseThrow(() -> new IllegalArgumentException("해당 유저 없음"));

        Review review = new Review();
        review.setTitle(dto.getTitle());
        review.setAuthor(dto.getAuthor());
        review.setThumbnail(dto.getThumbnail());
        review.setRating(dto.getRating());
        review.setContent(dto.getContent());
        review.setUser(user);  // ✅ 핵심!
        review.setCreatedAt(LocalDateTime.now());

        Review saved = reviewRepository.save(review);
        return ReviewDto.fromEntity(saved);
    }

    @GetMapping
    public List<ReviewDto> getReviewsByUser(@RequestParam String username) {
        List<Review> reviews = reviewService.getReviewsByUsername(username);
        return reviews.stream()
                .map(ReviewDto::fromEntity)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(@PathVariable Long id, @RequestBody ReviewDto dto) {
        return reviewService.updateReview(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReviewById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getReviewStats(@RequestParam String username) {
        List<Review> reviews = reviewRepository.findByUser_Username(username);

        // ⭐ 1. 별점 분포
        Map<Integer, Long> ratingCounts = reviews.stream()
                .collect(Collectors.groupingBy(Review::getRating, Collectors.counting()));

        // 📅 2. 월별 리뷰 수 (yyyy-MM)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        Map<String, Long> monthlyCounts = reviews.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getCreatedAt().format(formatter),
                        Collectors.counting()
                ));

        // ☁️ 3. 자주 쓴 단어 (감상평에서 단어 분리)
        Map<String, Long> wordFreq = reviews.stream()
                .map(Review::getContent)
                .filter(Objects::nonNull)
                .flatMap(content -> Arrays.stream(content.split("[\\s,.!?\\-]+")))
                .map(String::trim)
                .filter(word -> word.length() >= 2)
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()));

        ReviewStatsDto dto = ReviewStatsDto.builder()
                .ratingCounts(ratingCounts)
                .monthlyCounts(monthlyCounts)
                .wordFrequencies(wordFreq)
                .build();

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/all")
    public List<ReviewDto> getAllReviews() {
        List<Review> reviews = reviewRepository.findAllWithUser();
        return reviews.stream()
                .map(ReviewDto::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long id) {
        Optional<Review> optionalReview = reviewRepository.findById(id);
        if (optionalReview.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Review review = optionalReview.get();

        // ✅ 조회수 증가
        review.setViewCount(review.getViewCount() + 1);
        reviewRepository.save(review);

        return ResponseEntity.ok(ReviewDto.fromEntity(review));
    }

}
