<template>
  <div class="book-search">
    <h2>🔍 책 검색 및 등록</h2>
    <div class="search-bar">
      <input v-model="query" @keyup.enter="searchBooks" placeholder="책 제목을 입력하세요" />
      <button @click="searchBooks">검색</button>
    </div>
    <div class="book-grid" v-if="books.length">
        <div class="book-card" v-for="book in books" :key="book.isbn">
            <img :src="book.thumbnail || defaultCover" alt="book cover" />
            <div class="info">
                <h3>{{ book.title }}</h3>
                <p class="authors">{{ book.authors?.join(', ') }}</p>
            </div>
            <div class="card-footer">
                <select v-model="book.selectedStatus">
                <option disabled value="">책 상태 선택</option>
                <option value="TO_READ">📌 읽고 싶은 책</option>
                <option value="READING">📖 읽고 있는 책</option>
                <option value="DONE">✅ 다 읽은 책</option>
                </select>
                <button class="register-btn" @click="saveBook(book)">등록</button>
            </div>
        </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const query = ref('')
const books = ref([])
const defaultCover = 'https://via.placeholder.com/120x160?text=No+Image'
const loginUsername = ref(localStorage.getItem("username") || "")

const searchBooks = async () => {
  const REST_API_KEY = 'a65ac85ed4692e8e4f505ac8e66e1354'
  try {
    const res = await axios.get('/v3/search/book', {
      params: { query: query.value },
      headers: {
        Authorization: `KakaoAK ${REST_API_KEY}`
      }
    })
    books.value = res.data.documents
  } catch (err) {
    console.error('책 검색 실패:', err)
  }
}

const saveBook = async (book) => {
    if (!book.selectedStatus) {
        alert('책 상태를 선택해주세요!')
        return
    }
    try {
        await axios.post('/api/books', {
        title: book.title,
        authors: Array.isArray(book.authors) ? book.authors.join(', ') : book.authors,
        isbn: book.isbn,
        thumbnail: book.thumbnail || '',
        status: book.selectedStatus, // ✅ 선택된 상태 보내기
        user: { username: loginUsername.value }
        })
        alert('✅ 책이 등록되었습니다!')
    } catch (err) {
      if (err.response?.status === 409) {
        alert('⚠️ 이미 등록된 책입니다.');
      } else {
        console.error('책 등록 실패:', err);
        alert('❌ 책 등록 중 오류가 발생했습니다.');
      }
    }
}
</script>

<style scoped>
.book-search {
  padding: 2rem;
}

.search-bar {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 2rem;
}

.search-bar input {
  flex: 1;
  padding: 0.6rem;
  border: 1px solid #ccc;
  border-radius: 6px;
}

.search-bar button {
  padding: 0.6rem 1.2rem;
  background: #f5c518;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: bold;
}

.book-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 2.5rem;
  justify-items: center;
  padding-bottom: 3rem;
}

.book-card {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 1.6rem 1rem;
  width: 100%;
  max-width: 240px;
  box-sizing: border-box;
  transition: all 0.2s ease;
}

.book-card:hover {
  transform: translateY(-6px);
}

.book-card img {
  width: 140px;
  height: 200px;
  object-fit: cover;
  border-radius: 6px;
  margin-bottom: 1rem;
}

.info {
  text-align: center;
  margin-bottom: 1rem;
}

.info h3 {
  font-size: 1.05rem;
  font-weight: bold;
  color: #222;
  margin-bottom: 0.4rem;
}

.authors {
  color: #777;
  font-size: 0.85rem;
}

.card-footer {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  width: 100%;
  margin-top: auto;
}

.card-footer select {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 0.9rem;
  background: white;
  appearance: none;
}

.register-btn {
  width: 100%;
  padding: 0.5rem;
  background-color: #5cb85c;
  color: white;
  border: none;
  border-radius: 8px;
  font-weight: bold;
  cursor: pointer;
  font-size: 0.95rem;
  transition: background 0.2s;
}

.register-btn:hover {
  background-color: #4cae4c;
}

</style>