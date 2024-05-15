<script setup>
import { setStore } from '../store/setStore';
import { useRoute,useRouter } from 'vue-router'
const router=useRouter()
const route=useRoute()
const sys=setStore()

const currentPath = router.currentRoute.value.path;
    // 分割路径，并只取前两项
const pathParts = currentPath.split('/').slice(0, 4)
    // 将这两项重新组合成一个路径
const activePath = pathParts.join('/')
</script>

<template>
<el-container>
    <transition name="fade">
    
      <el-aside v-show="!sys.isCollapse" class="sub-aside"  ><!--副侧边栏-->
          
          <el-menu class="sub-menu" router="true" :default-openeds="['1']" :default-active="activePath">
              <el-sub-menu index="1">
                  <template #title>
                         <span style="padding-left:20px">我的文件</span> 
                  </template>
                  <el-menu-item class="sub-menu-item" index="/homepage/home/all" >
                      
                      <el-icon><Menu /></el-icon>
                      <span slot="title">全部</span>
                  
                  </el-menu-item>
                  <el-menu-item class="sub-menu-item" index="/homepage/home/image" >
                      
                      <el-icon><Picture /></el-icon>
                      <span slot="title">图片</span>
                  
                  </el-menu-item>
                      
                  <el-menu-item class="sub-menu-item" index="/homepage/home/document">
                      
                      <el-icon><Document /></el-icon>
                      <span slot="title">文档</span>
                      
                  </el-menu-item>
  
                  <el-menu-item class="sub-menu-item" index="/homepage/home/video">
                  
                      <el-icon><VideoPlay /></el-icon>
                      <span slot="title">视频</span>
                  
                  </el-menu-item>
  
                  <el-menu-item class="sub-menu-item" index="/homepage/home/audio">
                  
                      <el-icon><Headset /></el-icon>
                      <span slot="title">音频</span>
              
                  </el-menu-item> 
                  <el-menu-item class="sub-menu-item" index="/homepage/home/zip">
                  
                      <el-icon><Box /></el-icon>
                      <span slot="title">压缩包</span>
          
                  </el-menu-item> 
                  
                  <el-menu-item class="sub-menu-item" index="/homepage/home/others">
                      <el-icon><Star /></el-icon>
                      <span slot="title">其他</span>
          
                  </el-menu-item> 
              </el-sub-menu>
              
          </el-menu>
          
      </el-aside>
   </transition>
            <el-main><!--主体-->
              <router-view></router-view>
            </el-main>
  
    
</el-container>
</template>

<style scoped>
*{
  border: none;
  margin: 0;
 
}
.sub-aside{
  width: 160px;
  border-right: 1px  solid #e7e7e7;
  height: 100%;
  padding: 8px;
  transition: .2s;
}
.sub-menu{
  --el-menu-bg-color:#ffffff ;
  --el-menu-text-color:rgb(54, 54, 54) ;
  --el-menu-hover-bg-color:#f4faff;
  
}

.sub-menu-item{
  padding-left: 35px !important;
  margin: 2px;
  height: 45px;
}

.sub-menu-item:hover {
  --el-menu-text-color:#1197fe !important;
  border-radius: 12px;
}
.sub-menu-item.is-active {
  background-color: #e1f2ff;
  color: #1197fe ;
  border-radius: 12px;
  
}
.fade-enter-active,.fade-leave-active {
    transition: all .1s;
  }
  .fade-enter,.fade-leave-to {
    opacity: 0;
  }
  .fade-enter-to,.fade-leave {
    opacity: 1;
  }
  
</style>