<template>
  <div v-if="review && review.content" class="review-card">
    <img :src="review.thumbnail || '/default.jpg'" alt="book cover" class="thumbnail" />
    <div class="content">
      <h3 class="title">{{ review.title || '제목 없음' }}</h3>
      <p class="author">{{ review.author || '저자 미상' }}</p>
      <div class="meta">
        <span class="username">@{{ review.username || '익명' }}</span>
        <span class="rating">⭐ {{ review.rating || 0 }}/5</span>
      </div>
      <p class="excerpt">{{ review.content?.slice(0, 80) ?? '' }}</p>
    </div>
  </div>
</template>


<script>
export default {
  name: 'ReviewCard',
  props: {
    review: {
      type: Object,
      required: true,
      default: () => ({})
    }
  }
}
</script>

<style scoped>
.review-card {
  display: flex;
  width: 100%;
  max-width: 400px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: transform 0.2s ease;
}
.review-card:hover {
  transform: translateY(-5px);
}
.thumbnail {
  width: 100px;
  height: 150px;
  object-fit: cover;
}
.content {
  padding: 1rem;
  flex: 1;
}
.title {
  font-size: 1.1rem;
  margin: 0;
  font-weight: bold;
}
.author {
  font-size: 0.9rem;
  color: #777;
  margin-bottom: 0.5rem;
}
.meta {
  font-size: 0.8rem;
  color: #444;
  display: flex;
  justify-content: space-between;
  margin-bottom: 0.5rem;
}
.excerpt {
  font-size: 0.9rem;
  color: #333;
}
</style>
