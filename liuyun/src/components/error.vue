<script setup>
import { useRouter } from 'vue-router';

const router=useRouter()
function gotologin(){
  router.push("login")
}
</script>

<template>
<div class="error">
    <el-result
        icon="error"
        title="错误"
        sub-title="未登录或登录状态已过期"
      >
        <template #extra>
          <el-button type="primary" @click="gotologin">返回登录</el-button>
        </template>
    </el-result>
</div>
</template>

<style scoped>
.error{
  display: flex;
  align-items: center;
  justify-content: center;
  height: 95vh;
}
</style>
