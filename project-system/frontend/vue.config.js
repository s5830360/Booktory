const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,
  outputDir: '../src/main/resources/static', //빌드 타켓 디렉터리 (npm run build - 프로젝트를 완성해서 static 밑에 꽂아달라)
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8000',
        changeOrigin: true
      },
      '/v3': {
        target: 'https://dapi.kakao.com',
        changeOrigin: true,
        secure: false
      }
    }
  }
})
