<template>
  <div class="all-reviews">
    <h2>📚 전체 서평 모아보기</h2>
    <div class="top-bar">
      <input class="search-input" v-model="searchQuery" placeholder="🔍 책 제목 또는 작성자 검색" />
      <div class="sort-dropdown">
        <label for="sort">정렬 기준:</label>
        <select id="sort" v-model="sortOption" @change="sortReviews">
          <option value="latest">📅 최신순</option>
          <option value="rating">⭐ 별점 높은 순</option>
          <option value="likes">❤️ 좋아요순</option>
        </select>
      </div>
    </div>

    <div class="grid">
      <div v-for="review in sortedReviews" :key="review?.id" class="card">
        <router-link :to="`/review/${review.id}`" class="card-link">
          <img :src="review.thumbnail || defaultCover" alt="책 표지" />
          <div class="info">
            <h3>{{ review.title }}</h3>
            <p class="author">👤 {{ review.username }}</p>
            <p class="rating">⭐ {{ review.rating }}/5</p>
            <p class="content">{{ truncate(review.content, 80) }}</p>
            <p class="time">🕒 {{ formatDate(review.createdAt) }}</p>
          </div>
        </router-link>
        <button class="like-btn" @click.stop="toggleLike(review.id)">
          <span :style="{ color: likedMap[review.id] ? 'red' : '#aaa' }">
            {{ likedMap[review.id] ? '❤️' : '🤍' }}
          </span>
          {{ reviewLikes[review.id] || 0 }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'

const reviews = ref([])
const sortOption = ref('latest')
const defaultCover = '/default-cover.png'
const reviewLikes = ref({})
const searchQuery = ref("")
const likedMap = ref({})

const fetchReviews = async () => {
  try {
    const res = await axios.get('/api/reviews/all')
    reviews.value = res.data
    sortReviews()
  } catch (err) {
    console.error('서평 불러오기 실패:', err)
  }
}

const sortReviews = () => {}
const formatDate = (isoStr) => new Date(isoStr).toLocaleString()
const truncate = (str, length) => str.length > length ? str.slice(0, length) + '...' : str

const fetchLikeCount = async (reviewId) => {
  try {
    const res = await axios.get("/api/review-likes/count", { params: { reviewId } })
    reviewLikes.value[reviewId] = res.data
  } catch (err) {
    console.error("❤️ 좋아요 수 불러오기 실패", err)
  }
}

const toggleLike = async (reviewId) => {
  const username = localStorage.getItem("username")
  if (!username) {
    alert("로그인이 필요합니다.")
    return
  }

  const currentlyLiked = likedMap.value[reviewId] === true
  likedMap.value[reviewId] = !currentlyLiked
  reviewLikes.value[reviewId] = (reviewLikes.value[reviewId] || 0) + (currentlyLiked ? -1 : 1)

  try {
    await axios.post("/api/review-likes", null, { params: { reviewId, username } })
  } catch (err) {
    console.error("❤️ 좋아요 토글 실패", err)
    likedMap.value[reviewId] = currentlyLiked
    reviewLikes.value[reviewId] = (reviewLikes.value[reviewId] || 0) + (currentlyLiked ? 1 : -1)
  }
}

const sortedReviews = computed(() => {
  let filtered = [...reviews.value]
  if (searchQuery.value.trim()) {
    const keyword = searchQuery.value.toLowerCase()
    filtered = filtered.filter(r =>
      r.title?.toLowerCase().includes(keyword) ||
      r.username?.toLowerCase().includes(keyword)
    )
  }
  if (sortOption.value === 'latest') {
    return filtered.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
  } else if (sortOption.value === 'rating') {
    return filtered.sort((a, b) => b.rating - a.rating)
  } else if (sortOption.value === 'likes') {
    return filtered.sort((a, b) => (reviewLikes.value[b.id] || 0) - (reviewLikes.value[a.id] || 0))
  }
  return filtered
})

const fetchLikedStatus = async (reviewId) => {
  const username = localStorage.getItem("username")
  if (!username) return
  try {
    const res = await axios.get("/api/review-likes/is-liked", { params: { reviewId, username } })
    likedMap.value[reviewId] = res.data
  } catch (err) {
    console.error("좋아요 여부 확인 실패", err)
  }
}

onMounted(async () => {
  await fetchReviews()
  for (const review of reviews.value) {
    await fetchLikeCount(review.id)
    await fetchLikedStatus(review.id)
  }
})
</script>

<style scoped>
.all-reviews {
  padding: 2rem;
}

.top-bar {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  margin-bottom: 2rem;
  align-items: center;
  justify-content: space-between;
}

.search-input {
  flex: 1;
  min-width: 240px;
  max-width: 400px;
  padding: 0.6rem 1rem;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 12px;
  outline: none;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

.sort-dropdown {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.sort-dropdown label {
  font-weight: bold;
  font-size: 0.95rem;
}

.sort-dropdown select {
  padding: 0.5rem 1rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  background: white;
  font-size: 0.95rem;
  cursor: pointer;
  transition: background 0.2s;
}

.sort-dropdown select:hover {
  background-color: #f5f5f5;
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 2rem;
}

.card {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.04);
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  text-decoration: none;
  color: inherit;
  transition: transform 0.2s ease;
  position: relative;
}

.card:hover {
  transform: translateY(-6px);
}

.card-link {
  text-decoration: none;
  color: inherit;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.card img {
  width: 100%;
  height: 220px;
  object-fit: contain;
  background-color: #f9f9f9;
  border-radius: 12px;
  padding: 0.5rem;
}

.info {
  display: flex;
  flex-direction: column;
  gap: 0.2rem;
}

.card h3 {
  font-size: 1.1rem;
  font-weight: 600;
  color: #222;
  margin-bottom: 0.4rem;
}

.author,
.time,
.rating {
  font-size: 0.85rem;
  color: #666;
}

.content {
  font-size: 0.9rem;
  color: #333;
  margin: 0.5rem 0;
  line-height: 1.5;
}

.like-btn {
  align-self: center;
  background: #ffe5e5;
  border: none;
  border-radius: 20px;
  padding: 0.4rem 0.8rem;
  margin-top: 1rem;
  cursor: pointer;
  font-size: 0.9rem;
  font-weight: bold;
  transition: background 0.2s;
  box-shadow: 0 2px 4px rgba(255, 0, 0, 0.1);
}

.like-btn:hover {
  background-color: #ffc1c1;
}
</style>
