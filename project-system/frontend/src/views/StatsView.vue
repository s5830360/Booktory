<template>
  <div class="review-stats">
    <div v-if="stats">
      <div class="chart-box">
        <h3>⭐ 별점 분포</h3>
        <canvas id="ratingChart"></canvas>
      </div>

      <div class="chart-box">
        <h3>📅 월별 서평 수</h3>
        <canvas id="monthlyChart"></canvas>
      </div>

      <div class="word-cloud-box">
        <h3>☁️ 자주 사용한 단어</h3>
        <ul class="word-cloud">
          <li v-for="(count, word) in stats.wordFrequencies" :key="word">
            {{ word }} ({{ count }})
          </li>
        </ul>
      </div>
    </div>

    <div v-else>
      <p>📡 통계 데이터를 불러오는 중입니다...</p>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, nextTick } from 'vue'
import axios from 'axios'
import Chart from 'chart.js/auto'

const stats = ref(null)

onMounted(async () => {
  const username = localStorage.getItem('username')
  if (!username) return

  const res = await axios.get(`/api/reviews/stats`, {
    params: { username }
  })

  stats.value = res.data

  await nextTick()
  drawCharts()
})

const drawCharts = () => {
  if (!stats.value) return

  // ⭐ 별점 분포 차트
  const ratingCtx = document.getElementById('ratingChart')
  new Chart(ratingCtx, {
    type: 'bar',
    data: {
      labels: Object.keys(stats.value.ratingCounts),
      datasets: [{
        label: '개수',
        data: Object.values(stats.value.ratingCounts),
        backgroundColor: '#f5c518'
      }]
    }
  })

  // 📅 월별 서평 수 차트
  const monthlyCtx = document.getElementById('monthlyChart')
  new Chart(monthlyCtx, {
    type: 'line',
    data: {
      labels: Object.keys(stats.value.monthlyCounts),
      datasets: [{
        label: '서평 수',
        data: Object.values(stats.value.monthlyCounts),
        borderColor: '#4e6ef2',
        fill: false
      }]
    }
  })
}
</script>

<style scoped>
.review-stats {
  padding: 2rem;
  max-width: 800px;
  margin: auto;
}
.chart-box {
  margin-bottom: 2rem;
}
.word-cloud-box {
  background: #f9f9f9;
  padding: 1rem;
  border-radius: 8px;
}
.word-cloud {
  list-style: none;
  padding: 0;
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
}
.word-cloud li {
  font-size: 1.1rem;
  background: #e7ebff;
  padding: 0.4rem 0.8rem;
  border-radius: 6px;
}
</style>
