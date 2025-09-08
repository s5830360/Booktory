<template>
  <div class="my-reviews">
    <h2>📖 내가 쓴 서평</h2>
    <div class="grid">
      <div class="card" v-for="review in reviews" :key="review.id">
        <img :src="review.thumbnail || defaultCover" alt="표지" />
        <div class="info">
            <h3>{{ review.title }}</h3>
            <p>👤 {{ review.username }}</p>
            <p>⭐ {{ review.rating }}/5</p>
            <p>{{ review.content }}</p>
            <p class="time">🕒 {{ formatDate(review.createdAt) }}</p>
            <router-link :to="`/edit/${review.id}`">
                <button class="edit-btn">✏️ 수정</button>
            </router-link>
            <button class="delete-btn" @click="deleteReview(review.id)">🗑️ 삭제</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const reviews = ref([])
const defaultCover = '/default-cover.png'

const formatDate = (isoStr) => new Date(isoStr).toLocaleString()

onMounted(async () => {
  const username = localStorage.getItem('username')
  if (!username) return

  const res = await axios.get(`/api/reviews`, {
    params: { username }
  })
  reviews.value = res.data
})

const deleteReview = async (id) => {
  if (!confirm('정말 삭제하시겠습니까?')) return;

  try {
    await axios.delete(`/api/reviews/${id}`);
    reviews.value = reviews.value.filter(r => r.id !== id); // 화면에서 제거
    alert('삭제 완료!');
  } catch (err) {
    console.error('삭제 실패:', err);
    alert('삭제 중 오류 발생');
  }
}
</script>

<style scoped>
.my-reviews {
  padding: 2rem;
}
.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1.5rem;
}
.card {
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.05);
  padding: 1rem;
}
.card img {
  width: 100px;
  height: 140px;
  object-fit: cover;
  margin-bottom: 0.5rem;
}
.info {
  font-size: 0.9rem;
}
.time {
  color: #666;
  font-size: 0.8rem;
  margin-top: 0.3rem;
}
.edit-btn,
.delete-btn {
  padding: 0.4rem 0.8rem;
  border-radius: 6px;
  font-weight: bold;
  border: none;
  cursor: pointer;
  margin-right: 0.5rem;
}

.edit-btn {
  background-color: #f5c518;
  color: #222;
  transition: background-color 0.2s;
}

.edit-btn:hover {
  background-color: #e6b800;
}

.delete-btn {
  background-color: #e74c3c;
  color: white;
}

.delete-btn:hover {
  background-color: #c0392b;
}

</style>
