<template>
  <div class="book-list">
    <h2>📚 내가 등록한 책</h2>
    <div class="status-tabs">
        <button :class="['tab-btn', { active: tab === 'TO_READ' }]" @click="tab = 'TO_READ'">📌 읽고 싶은 책</button>
        <button :class="['tab-btn', { active: tab === 'READING' }]" @click="tab = 'READING'">📖 읽고 있는 책</button>
        <button :class="['tab-btn', { active: tab === 'DONE' }]" @click="tab = 'DONE'">✅ 다 읽은 책</button>
    </div>
    <div class="grid">
    <template v-if="books.filter(b => b?.id && b.status === tab).length > 0">
      <template v-for="book in books.filter(b => b?.id && b.status === tab)" :key="book.id">
        <div class="card">
          <img :src="book.thumbnail || defaultCover" alt="표지" />
          <div class="info">
            <h3>{{ book.title }}</h3>
            <p>{{ book.authors }}</p>
            <select v-model="book.status" @change="updateStatus(book)">
              <option value="TO_READ">📌 읽고 싶은 책</option>
              <option value="READING">📖 읽고 있는 책</option>
              <option value="DONE">✅ 다 읽은 책</option>
            </select>
            <div class="button-row">
              <router-link :to="`/write/${book.id}`" style="display: contents;">
              <button class="review-btn">✍️ 서평 쓰기</button>
              </router-link>
              <button class="delete-btn" @click="deleteBook(book.id)">🗑️ 삭제</button>
            </div> 
          </div>
        </div>
      </template>
    </template>
    <div v-else class="empty-message">
      {{ tabMessage }}
    </div>
</div>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { computed } from 'vue'

const tabMessage = computed(() => {
  switch (tab.value) {
    case 'TO_READ':
      return '📌 읽고 싶은 책이 없어요. 책 검색 페이지에서 추가해보세요!';
    case 'READING':
      return '📖 읽고 있는 책이 없어요. 책을 선택해 읽기 시작해보세요!';
    case 'DONE':
      return '✅ 다 읽은 책이 없어요. 책을 다 읽고 서평을 남겨보세요!';
    default:
      return '';
  }
})

const books = ref([])
const defaultCover = '/default-cover.png'
const tab = ref('TO_READ')
const loginUsername = ref(localStorage.getItem("username") || "")

// 책 조회
const fetchBooks = async () => {
  try {
    const res = await axios.get('/api/books', {
      params: { username: loginUsername.value }
    });
    books.value = res.data;
  } catch (err) {
    console.error('책 불러오기 실패:', err);
  }
};

const updateStatus = async (book) => {
  try {
    await axios.patch(`/api/books/${book.id}`, {
      status: book.status
    })
  } catch (err) {
    console.error('책 상태 업데이트 실패:', err)
  }
}

const deleteBook = async (id) => {
  if (!confirm('정말로 이 책을 삭제하시겠습니까?')) return;
  try {
    await axios.delete(`/api/books/${id}`);
    books.value = books.value.filter(book => book.id !== id);
  } catch (err) {
    console.error('삭제 실패:', err);
  }
}

onMounted(() => {
  fetchBooks()
})
</script>

<style scoped>
.book-list {
  padding: 2rem;
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 1.5rem;
}

.info {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex-grow: 1;
}

.card {
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  padding: 1rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  transition: transform 0.2s;
  height: 380px;
  text-align: center;
}

.card h3 {
  font-size: 1.1rem;
  margin: 0.5rem 0 0.3rem;
}

.card p {
  font-size: 0.9rem;
  color: #666;
  margin-bottom: auto;
}

.card select {
  margin-top: 0.5rem;
  padding: 0.3rem 0.5rem;
  border-radius: 6px;
  border: 1px solid #ccc;
  font-size: 0.9rem;
}

.card:hover {
  transform: translateY(-4px);
}

.card img {
  width: 120px;
  height: 160px;
  object-fit: cover;
  margin-bottom: 0.6rem;
}

.status-tabs {
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-bottom: 2rem;
}

.tab-btn {
  padding: 0.5rem 1rem;
  border-radius: 20px;
  border: 1px solid #f5c518;
  background-color: #fffbe6;
  font-weight: 500;
  color: #b08500;
  cursor: pointer;
  transition: all 0.2s;
}

.tab-btn:hover {
  background-color: #fff2b3;
}

.tab-btn.active {
  background-color: #f5c518;
  color: #222;
  border: none;
}

.review-btn {
  margin-top: 0.5rem;
  padding: 0.5rem 1rem;
  background-color: #f5c518;
  color: #222;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: bold;
  transition: background 0.2s;
}

.review-btn:hover {
  background-color: #e6b800;
}

.empty-message {
  grid-column: 1 / -1;
  text-align: center;
  color: #777;
  font-size: 1.1rem;
  padding: 2rem 0;
}

.delete-btn {
  margin-top: 0.5rem;
  background-color: #ff4d4f;
  color: white;
  border: none;
  border-radius: 6px;
  padding: 0.4rem 0.8rem;
  cursor: pointer;
  font-weight: bold;
  transition: background 0.2s;
}

.delete-btn:hover {
  background-color: #d9363e;
}

.button-row {
  display: flex;
  gap: 0.5rem;
  margin-top: 0.6rem;
}
</style>
