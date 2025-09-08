package com.example.project_system.repository;

import com.example.project_system.entity.Review;
import com.example.project_system.entity.ReviewLike;
import com.example.project_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    Optional<ReviewLike> findByReviewAndUser(Review review, User user);
    long countByReview(Review review);
}
