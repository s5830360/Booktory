<template>
  <div class="home">
    <!-- Hero Section -->
    <section class="hero">
      <div class="introduce">
        <div class="logo"><img :src="BooktoryLogo" alt="Booktory 로고" /></div>
        <h1>나만의 독서 기록 플랫폼</h1>
      </div>
      <p>읽은 책을 기록하고 서평을 남기며 나만의 서재를 만들어보세요.</p>
    </section>

    <!-- 최근 서평 섹션 -->
    <section class="recent-reviews">
      <h2>✨ 최신 서평</h2>
      <div v-if="recentReviews.length > 0" class="review-list">
        <ReviewCard
          v-for="review in recentReviews"
          :key="review.id"
          :review="review"
        />
      </div>
      <div v-else class="no-reviews">
        <p>등록된 서평이 없습니다. 첫 서평을 작성해보세요!</p>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import ReviewCard from '@/components/ReviewCard.vue'
import BooktoryLogo from '@/assets/Booktory.png'

const recentReviews = ref([])

onMounted(async () => {
  try {
    const res = await fetch('http://localhost:8000/api/reviews/recent')
    recentReviews.value = await res.json()
  } catch (err) {
    console.error('서평 불러오기 실패:', err)
    recentReviews.value = []
  }
})
</script>

<style scoped>
.home {
  max-width: 900px;
  margin: auto;
  padding: 2rem;
  font-family: 'Noto Sans KR', sans-serif;
}
.hero {
  text-align: center;
  margin-bottom: 2rem;
}
.recent-reviews {
  margin-bottom: 2rem;
}
.review-list {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
}
.btn {
  margin: 0 1rem;
  padding: 0.75rem 1.5rem;
  background: #f5c518;
  border-radius: 8px;
  color: black;
  text-decoration: none;
  font-weight: bold;
}
.no-reviews {
  text-align: center;
  color: #666;
  margin-top: 2rem;
}
.logo img {
  height: 50px;
}
.introduce {
  display: flex;
  align-items: center; /* 세로 가운데 정렬 */
  justify-content: center; /* 전체 가운데 정렬 */
  gap: 1rem; /* 이미지와 텍스트 사이 간격 */
}
</style>
