<template>
  <div class="mypage">
    <div v-if="userInfo" class="profile-card">
      <div class="profile-header">
        <span class="icon">👤</span>
        <h2>{{ userInfo.nickname }}님</h2>
        <button @click="toggleEdit" class="edit-btn">
          {{ editing ? '저장' : '✏️ 수정' }}
        </button>
      </div>

      <div v-if="editing" class="edit-section">
        <input v-model="editedNickname" class="edit-input" />
      </div>

      <div class="info-list">
        <div class="info-item">
          <span>📅 가입일</span>
          <strong>{{ userInfo.joinedDate }}</strong>
        </div>
        <div class="info-item">
          <span>📝 작성한 서평 수</span>
          <strong>{{ userInfo.reviewCount }}개</strong>
        </div>
        <div class="info-item">
          <span>⭐ 평균 별점</span>
          <strong>{{ userInfo.averageRating }} / 5</strong>
        </div>
      </div>
    </div>

    <div v-else class="no-user">
      🔒 로그인 정보가 없습니다. 먼저 로그인 해주세요.
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const userInfo = ref(null)
const editing = ref(false)
const editedNickname = ref('')

const loadUserInfo = async () => {
  const username = localStorage.getItem('username')
  if (!username) return

  const res = await axios.get(`/api/users/${username}`)
  const data = res.data
  userInfo.value = {
    username: data.username,
    nickname: data.nickname || data.username,
    joinedDate: new Date(data.joinedDate).toLocaleDateString(),
    reviewCount: data.reviewCount,
    averageRating: data.averageRating.toFixed(1)
  }
  editedNickname.value = userInfo.value.nickname
}

onMounted(loadUserInfo)

const toggleEdit = async () => {
  if (!editing.value) {
    editing.value = true
    return
  }

  try {
    await axios.patch(`/api/users/${userInfo.value.username}/nickname`, {
      nickname: editedNickname.value
    })

    localStorage.setItem('nickname', editedNickname.value)
    window.dispatchEvent(new Event('nickname-updated'))

    await loadUserInfo() // 💡 닉네임 수정 후 다시 로딩
    alert('닉네임이 수정되었습니다!')
  } catch (err) {
    console.error('닉네임 수정 실패:', err)
    alert('수정 실패!')
  } finally {
    editing.value = false
  }
}

</script>

<style scoped>
.mypage {
  display: flex;
  justify-content: center;
  padding: 3rem 1rem;
}

.profile-card {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  padding: 2rem 2.5rem;
  width: 100%;
  max-width: 500px;
}

.profile-header {
  display: flex;
  align-items: center;
  gap: 0.8rem;
  justify-content: space-between;
  margin-bottom: 1.2rem;
}

.icon {
  font-size: 2rem;
}

.edit-btn {
  background-color: #f5c518;
  border: none;
  padding: 0.4rem 0.9rem;
  border-radius: 6px;
  font-weight: bold;
  cursor: pointer;
}

.edit-section {
  display: flex;
  justify-content: center;
  margin-bottom: 1rem;
}

.edit-input {
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 6px;
  width: 100%;
  font-size: 1rem;
}

.info-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.info-item {
  display: flex;
  justify-content: space-between;
  font-size: 1rem;
  border-bottom: 1px solid #eee;
  padding-bottom: 0.4rem;
}

.no-user {
  font-size: 1.1rem;
  text-align: center;
  margin-top: 5rem;
  color: #666;
}
</style>
