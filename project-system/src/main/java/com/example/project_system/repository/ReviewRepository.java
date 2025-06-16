package com.example.project_system.repository;

import com.example.project_system.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 최신 서평 5개 가져오기 (내림차순 정렬)
    List<Review> findTop5ByOrderByCreatedAtDesc();

    List<Review> findByUser_Username(String username);

    int countByUser_Username(String username);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.user.username = :username")
    Double getAverageRatingByUsername(@Param("username") String username);

}

