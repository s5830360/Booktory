<template>
  <header class="header">
    <div class="logo" @click="goHome"><img :src="BooktoryLogo" alt="Booktory 로고" /></div>
    <div class="center-logo">
      <img :src="BooktoryText" alt="Booktory 텍스트 로고" />
    </div>
    <div class="actions">
      <span v-if="username">
        👤 {{ nickname }}님 |
        <button @click="logout">로그아웃</button>
      </span>
      <span v-else>
        <button @click="goLogin">로그인</button>
      </span>
    </div>
  </header>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import BooktoryLogo from '@/assets/Booktory.png'
import BooktoryText from '@/assets/Booktory_text.png'

const router = useRouter()
const username = ref('')
const nickname = ref('')

const syncUsername = () => {
  const user = localStorage.getItem('username')
  const nick = localStorage.getItem('nickname')  // ✅ localStorage에서 가져오기
  username.value = user && user !== 'undefined' ? user : ''
  nickname.value = nick && nick !== 'undefined' ? nick : ''
}

onMounted(() => {
  syncUsername()

  window.addEventListener("login-success", syncUsername)
  window.addEventListener("logout", syncUsername)
  window.addEventListener("nickname-updated", syncUsername)
})

onUnmounted(() => {
  window.removeEventListener("login-success", syncUsername)
  window.removeEventListener("logout", syncUsername)
  window.addEventListener("nickname-updated", syncUsername)
})

const logout = () => {
  localStorage.removeItem('username')
  localStorage.removeItem('nickname')
  syncUsername()
  window.dispatchEvent(new Event("logout"))
  router.push('/')
}

const goLogin = () => {
  router.push('/login')
}

const goHome = () => {
  router.push('/')
}
</script>

<style scoped>
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 2rem;
  background-color: #f5c518;
  font-weight: bold;
  z-index: 1000;
}

.logo {
  cursor: pointer;
}

.logo img {
  height: 50px;
  cursor: pointer;
}

.actions button {
  margin-left: 1rem;
  background: white;
  border: none;
  border-radius: 6px;
  padding: 0.4rem 0.8rem;
  cursor: pointer;
}

.center-logo {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  margin-top: 10px;
}

.center-logo img {
  height: 40px;
  opacity: 0.9;
}

</style>