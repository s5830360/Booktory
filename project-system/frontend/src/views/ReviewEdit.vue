<template>
  <div class="edit-review">
    <h2>✏️ 서평 수정</h2>

    <div class="card">
      <div class="form-group">
        <label>책 제목</label>
        <input type="text" v-model="title" readonly class="readonly-input" />
      </div>

      <div class="form-group">
        <label>별점</label>
        <select v-model="rating" class="input">
          <option v-for="n in 5" :key="n" :value="n">{{ n }}점</option>
        </select>
      </div>

      <div class="form-group">
        <label>감상평</label>
        <textarea v-model="content" class="input" placeholder="감상평을 입력하세요" />
      </div>

      <button class="submit-btn" @click="submitEdit">서평 수정</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const reviewId = route.params.reviewId

const title = ref('')
const rating = ref('')
const content = ref('')
const author = ref('')
const thumbnail = ref('')
const username = localStorage.getItem('username')

onMounted(async () => {
  const res = await axios.get(`/api/reviews/${reviewId}`)
  const review = res.data
  title.value = review.title
  rating.value = review.rating
  content.value = review.content
  author.value = review.author || ''
  thumbnail.value = review.thumbnail || ''
})

const submitEdit = async () => {
  try {
    await axios.put(`/api/reviews/${reviewId}`, {
      title: title.value || '제목 없음',
      author: author.value || '작자 미상',
      username: username || 'unknown',
      thumbnail: thumbnail.value || '',
      rating: rating.value || 0,
      content: content.value || '',
      createdAt: null
    })
    alert('서평이 수정되었습니다!')
    router.push('/reviews')
  } catch (err) {
    console.error('수정 오류:', err)
    alert('수정 실패!')
  }
}
</script>

<style scoped>
.edit-review {
  max-width: 600px;
  margin: 3rem auto;
}

.card {
  background: #fff;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
}

label {
  margin-bottom: 0.3rem;
  font-weight: 600;
}

.input,
.readonly-input,
textarea {
  padding: 0.75rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 1rem;
}

.readonly-input {
  background-color: #f9f9f9;
  font-weight: bold;
  color: #555;
}

.submit-btn {
  align-self: flex-end;
  padding: 0.7rem 1.2rem;
  background-color: #f5ce42;
  color: black;
  border: none;
  border-radius: 8px;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.2s;
}

.submit-btn:hover {
  background-color: #f8c306;
}
</style>
