<template>
  <div id="app">
    <HeaderView class="header" />
    <div class="container">
      <SideBar class="sidebar" />
      <main class="main-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import HeaderView from './components/HeaderView.vue';
import SideBar from './components/SideBar.vue';
</script>

<style scoped>
* {
  box-sizing: border-box;
}

#app {
  height: 100vh;
  display: flex;
  flex-direction: column;
  margin: 0;
  font-family: 'Segoe UI', sans-serif;
}

.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 60px;
  z-index: 1000;
}

.container {
  display: flex;
  flex: 1;
  height: 100%;
  padding-top: 60px; /* 헤더 높이만큼 */
}

.sidebar {
  position: fixed;
  top: 60px;
  left: 0;
  bottom: 0;
  width: 200px;
  background-color: #fff8dc; /* 밝은 크림색 */
  border-right: 1px solid #eee;
  padding: 1.5rem;
  z-index: 999;
}

.main-content {
  margin-left: 200px;
  flex: 1;
  padding: 0rem 2rem; /* 상단 간격 줄임 */
  background-color: #fafafa;
  overflow-y: auto;
  min-height: 0;
}
</style>
