<template>
  <nav class="sidebar">
    <router-link
      to="/search"
      :class="{ active: $route.path.startsWith('/search') }"
    >책 검색</router-link>

    <router-link
      to="/my"
      :class="{ active: $route.path === '/my' }"
    >내 독서 기록</router-link>

    <router-link
      to="/all"
      :class="{ active: $route.path.startsWith('/all') }"
    >전체 서평</router-link>

    <router-link
      to="/reviews"
      :class="{ active: $route.path.startsWith('/reviews') }"
    >내가 쓴 서평</router-link>

    <router-link
      to="/stats"
      :class="{ active: $route.path.startsWith('/stats') }"
    >통계 보기</router-link>

    <router-link
      to="/mypage"
      :class="{ active: $route.path.startsWith('/mypage') }"
    >마이페이지</router-link>
  </nav>
</template>

<style scoped>
.sidebar {
  width: 200px;
  background-color: #fff8dc; /* 밝은 노란색 베이스 */
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
  height: calc(100vh - 60px);
  position: sticky;
  top: 60px;
  border-right: 2px solid #f5c518; /* 구분선 강조 */
}

.sidebar a {
  text-decoration: none;
  color: #555;
  font-weight: 500;
  padding: 0.6rem 1rem;
  border-radius: 8px;
  margin-bottom: 0.4rem;
  transition: background 0.2s, color 0.2s;
}

.sidebar a:hover {
  background-color: #fff2b3; /* hover 시 더 진한 노랑 */
}

.sidebar a.active {
  background-color: #f5c518;
  color: white;
  font-weight: bold;
}
</style>
