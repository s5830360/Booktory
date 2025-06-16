package com.example.project_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ReviewStatsDto {
    private Map<Integer, Long> ratingCounts;         // 별점 분포 (ex: {5: 10, 4: 3})
    private Map<String, Long> monthlyCounts;         // 월별 리뷰 수 (ex: {"2025-06": 2})
    private Map<String, Long> wordFrequencies;       // 자주 쓴 단어 (ex: {"재밌음": 5})
}
