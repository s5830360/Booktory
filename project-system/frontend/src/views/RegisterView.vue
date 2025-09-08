<template>
  <div class="register-container">
    <h2>회원가입</h2>
    <form @submit.prevent="handleRegister">
      <label>아이디</label>
      <input v-model="username" placeholder="아이디 입력" required />

      <label>비밀번호</label>
      <input type="password" v-model="password" placeholder="비밀번호 입력" required />

      <label>이메일 (선택)</label>
      <input type="email" v-model="email" placeholder="email@example.com" />

      <button type="submit">회원가입</button>
      <p class="helper">이미 계정이 있으신가요? <router-link to="/login">로그인</router-link></p>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const username = ref('')
const password = ref('')
const email = ref('')
const router = useRouter()

async function handleRegister() {
  try {
    const res = await fetch('http://localhost:8000/api/auth/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        username: username.value,
        password: password.value,
        email: email.value
      })
    })

    const msg = await res.text()
    if (res.ok) {
      alert('🎉 회원가입 성공: ' + msg)
      router.push('/login')
    } else {
      alert('❌ 회원가입 실패: ' + msg)
    }
  } catch (err) {
    alert('서버 오류!')
    console.error(err)
  }
}
</script>

<style scoped>
.register-container {
  max-width: 400px;
  margin: 4rem auto;
  padding: 2rem;
  border-radius: 12px;
  background: #f9f9f9;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

form {
  display: flex;
  flex-direction: column;
}

input,
button {
  width: 100%;
  padding: 0.75rem;
  margin: 0.5rem 0 1rem;
  border-radius: 8px;
  box-sizing: border-box;
}

input {
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
