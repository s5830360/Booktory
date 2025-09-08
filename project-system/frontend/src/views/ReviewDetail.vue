<template>
  <div v-if="review" class="review-detail">
    <h2 class="title">📖 {{ review.title }}</h2>

    <ul class="meta-list">
        <li><span class="icon">👤 작성자:</span> {{ review.username }}</li>
        <li><span class="icon">⭐ 별점:</span> {{ review.rating }}/5</li>
        <li><span class="icon">🕒 작성일:</span> {{ formatDate(review.createdAt) }}</li>
        <li><span class="icon">👁️ 조회수:</span> {{ review.viewCount || 0 }}</li>
        <li class="like-row">
            <span class="icon">❤️ 좋아요:</span>
            <span class="count">{{ likeCount }}</span>
            <button class="like-btn" @click="toggleLike">
            <span :style="{ color: liked ? 'red' : '#aaa' }">
                {{ liked ? '❤️' : '🤍' }}
            </span>
            </button>
        </li>
    </ul>

    <div class="content-box">
      <p>{{ review.content }}</p>
    </div>

    <div class="btn-row">
      <router-link to="/all" class="back-link">← 전체 서평 보기</router-link>
      <button v-if="isMine" class="edit-btn" @click="editReview">✏️ 수정</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const router = useRouter()
const reviewId = route.params.id

const review = ref(null)
const likeCount = ref(0)
const liked = ref(false)

const formatDate = (isoStr) =>
  new Date(isoStr).toLocaleString()

const fetchReviewDetail = async () => {
  try {
    const res = await axios.get(`/api/reviews/${reviewId}`)
    review.value = res.data
  } catch (err) {
    console.error("서평 상세 조회 실패", err)
  }
}

const fetchLikeData = async () => {
  try {
    const username = localStorage.getItem("username")
    const countRes = await axios.get("/api/review-likes/count", {
      params: { reviewId }
    })
    likeCount.value = countRes.data

    if (username) {
      const likedRes = await axios.get("/api/review-likes/is-liked", {
        params: { reviewId, username }
      })
      liked.value = likedRes.data
    }
  } catch (err) {
    console.error("좋아요 데이터 로드 실패", err)
  }
}

const toggleLike = async () => {
  const username = localStorage.getItem("username")
  if (!username) {
    alert("로그인이 필요합니다")
    return
  }

  try {
    await axios.post("/api/review-likes", null, {
      params: { reviewId, username }
    })
    await fetchLikeData()
  } catch (err) {
    console.error("좋아요 토글 실패", err)
  }
}

const isMine = computed(() => {
  return localStorage.getItem("username") === review.value?.username;
});

const editReview = () => {
  router.push(`/edit/${review.value.id}`);
};

onMounted(async () => {
  await fetchReviewDetail()
  await fetchLikeData()
})
</script>

<style scoped>
.review-detail {
  background: #fff;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 3px 12px rgba(0, 0, 0, 0.05);
  max-width: 640px;
  margin: 2rem auto;
}

.title {
  font-size: 1.6rem;
  font-weight: bold;
  margin-bottom: 1rem;
}

.meta-list {
  list-style: none;
  padding: 0;
  margin: 0 0 1.5rem 0;
}

.meta-list li {
  margin-bottom: 0.5rem;
  font-size: 0.95rem;
}

.meta-list li span {
  font-weight: bold;
  margin-right: 0.4rem;
}

.like-btn {
  border: none;
  background: transparent;
  cursor: pointer;
  font-size: 1.1rem;
}

.content-box {
  margin-bottom: 1.5rem;
  font-size: 1rem;
}

.btn-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.edit-btn {
  background-color: #f5c518;
  color: #222;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 8px;
  font-weight: bold;
  cursor: pointer;
}

.edit-btn:hover {
  background-color: #e6b800;
}

.back-link {
  color: #0077cc;
  font-weight: bold;
  text-decoration: none;
}
</style>
