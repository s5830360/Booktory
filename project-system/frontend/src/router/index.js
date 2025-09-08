import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '@/views/LoginView.vue'
import RegisterView from '@/views/RegisterView.vue'
import BookSearch from '@/views/BookSearch.vue'
import BookList from '@/views/BookList.vue'
import ReviewWrite from '@/views/ReviewWrite.vue'
import MyReviews from '@/views/MyReviews.vue'
import ReviewEdit from '@/views/ReviewEdit.vue'
import MyPage from '@/views/MyPage.vue'
import StatsView from '@/views/StatsView.vue'
import AllReviews from '@/views/AllReviews.vue'

const routes = [
  { path: '/', name: 'home', component: HomeView },
  { path: '/login', component: LoginView }, 
  { path: '/register', component: RegisterView },
  { path: '/search', component: BookSearch },
  { path: '/my', component: BookList },
  { path: '/write/:bookId', component: ReviewWrite},
  { path: '/reviews', component: MyReviews},
  { path: '/edit/:reviewId', component: ReviewEdit},
  { path: '/mypage', component: MyPage},
  { path: '/stats', component: StatsView},
  { path: '/all', component: AllReviews},
  {
    path: '/review/:id',
    name: 'ReviewDetail',
    component: () => import('@/views/ReviewDetail.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
