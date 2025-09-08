package com.example.project_system.dto;

import com.example.project_system.entity.Review;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {
    private Long id;
    private String title;
    private String author;
    private String username;
    private String thumbnail;
    private int rating;
    private String content;
    private LocalDateTime createdAt;
    private Long viewCount;

    // 엔티티 → DTO 변환용 메서드
    public static ReviewDto fromEntity(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .title(review.getTitle())
                .author(review.getAuthor())
                .username(review.getUser() != null ? review.getUser().getUsername() : "(알 수 없음)")
                .thumbnail(review.getThumbnail())
                .rating(review.getRating())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .viewCount(review.getViewCount())
                .build();
    }

}
