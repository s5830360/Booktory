<template>
  <div class="login-container">
    <h2>로그인</h2>
    <form @submit.prevent="handleLogin">
      <label for="username">아이디</label>
      <input id="username" v-model="username" required />

      <label for="password">비밀번호</label>
      <input id="password" type="password" v-model="password" required />

      <button type="submit">로그인</button>
    </form>
    <p class="helper">
      계정이 없으신가요?
      <router-link to="/register">회원가입</router-link>
    </p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const username = ref('')
const password = ref('')
const router = useRouter()

async function handleLogin() {
  try {
    const res = await fetch('http://localhost:8000/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        username: username.value,
        password: password.value
      })
    })

    const data = await res.json()
    if (res.ok) {
      alert('✅ 로그인 성공: ' + data)
      localStorage.setItem('username', data.username)
      localStorage.setItem('nickname', data.nickname)
      window.dispatchEvent(new Event("login-success"))
      router.push('/')
    } else {
      alert('❌ 로그인 실패: ' + data.error)
    }
  } catch (err) {
    console.error('서버 연결 실패:', err)
    alert('서버 오류!')
  }
}
</script>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 4rem auto;
  padding: 2rem;
  border-radius: 12px;
  background: #f9f9f9;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}
input,
button {
  width: 100%;
  padding: 0.75rem;
  border-radius: 8px;
  box-sizing: border-box; /* ✅ 중요: padding 포함 폭 계산 */
}
input {
  margin: 0.5rem 0 1rem;
  border: 1px solid #ccc;
}

button {
  background: #f5c518;
  border: none;
  font-weight: bold;
}
.helper {
  margin-top: 1rem;
  font-size: 0.9rem;
  text-align: center;
}
</style>