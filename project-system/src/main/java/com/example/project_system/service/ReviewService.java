package com.example.project_system.service;

import com.example.project_system.entity.Review;
import com.example.project_system.entity.User;
import com.example.project_system.repository.ReviewRepository;
import com.example.project_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.project_system.dto.ReviewDto;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public List<Review> getRecentReviews() {
        return reviewRepository.findTop5ByOrderByCreatedAtDesc();
    }

    public Review createReview(ReviewDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저 없음"));

        Review review = new Review();
        review.setTitle(dto.getTitle());
        review.setAuthor(dto.getAuthor());
        review.setThumbnail(dto.getThumbnail());
        review.setRating(dto.getRating());
        review.setContent(dto.getContent());
        review.setUser(user); // ✅ 핵심!
        review.setCreatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByUsername(String username) {
        return reviewRepository.findByUser_Username(username);
    }

    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    public Optional<Review> updateReview(Long id, ReviewDto dto) {
        return reviewRepository.findById(id).map(review -> {
            review.setRating(dto.getRating());
            review.setContent(dto.getContent());
            return reviewRepository.save(review);
        });
    }

    public void deleteReviewById(Long id) {
        reviewRepository.deleteById(id);
    }

}
