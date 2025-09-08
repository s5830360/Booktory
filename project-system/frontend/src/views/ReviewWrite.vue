<template>
  <div class="review-form">
    <h2>✍️ 서평 작성</h2>

    <label>책 제목</label>
    <input type="text" v-model="title" readonly />

    <label>별점</label>
    <select v-model="rating" class="input">
      <option disabled value="">선택</option>
      <option v-for="n in 5" :key="n" :value="n">{{ n }}점</option>
    </select>

    <label>감상평</label>
    <textarea v-model="content" placeholder="감상평을 작성하세요" class="input"/>

    <button
      class="submit-btn"
      @click="submitReview"
      :disabled="status === 'TO_READ'"
    >
      {{ status === 'TO_READ' ? '읽고 싶은 책은 서평 작성 불가' : '서평 등록' }}
    </button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const router = useRouter()
const bookId = route.params.bookId
const title = ref('')
const rating = ref('')
const content = ref('')
const author = ref('')
const thumbnail = ref('')
const status = ref('')

onMounted(async () => {
    const res = await axios.get(`/api/books/${bookId}`)
    const book = res.data
    title.value = book.title
    author.value = book.authors
    thumbnail.value = book.thumbnail
    status.value = book.status
    console.log("책 상태 확인:", status.value)
})

const submitReview = async () => {
  if (!rating.value || !content.value) {
    alert('모든 항목을 입력해주세요!')
    return
  }

  await axios.post('/api/reviews', {
    title: title.value,
    author: author.value,
    thumbnail: thumbnail.value,
    rating: rating.value,
    content: content.value,
    username: localStorage.getItem('username')
  })

  alert('서평이 등록되었습니다!')
  router.push('/all')
}

</script>
<style scoped>
.review-form {
  max-width: 500px;
  margin: 2rem auto;
  background: #fff;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
  display: flex;
  flex-direction: column;
  gap: 1.2rem;
}

.input {
  width: 100%;
  padding: 0.8rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 1rem;
  box-sizing: border-box;
}

.submit-btn {
  width: 100%;
  padding: 0.9rem;
  background: #f5ce42;
  color: black;
  border: none;
  border-radius: 8px;
  font-weight: bold;
  font-size: 1rem;
  cursor: pointer;
  transition: background 0.2s;
}

.submit-btn:hover {
  background: #f8c306;
}

.submit-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>