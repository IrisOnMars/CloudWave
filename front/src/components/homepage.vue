<script setup>
import { ref,reactive } from 'vue'
import { useRoute,useRouter } from 'vue-router'
import {setStore} from "../store/setStore"
import {userStore} from "../store/userStore"
const router=useRouter()
const route=useRoute()
const sys=setStore()
const sysUser=userStore()
const asideWidth = ref("160px")



function fold(){
  sys.isCollapse=!sys.isCollapse
  asideWidth.value=sys.isCollapse?'76px':'160px'
}

async function loginOut(){
  router.push('/login')
}
function userInfo(){
  router.push('/homepage/user')
}
function activePath(){
  const currentPath = router.currentRoute.value.path;
    // 分割路径，并只取前两项
const pathParts = currentPath.split('/').slice(0, 3)
    // 将这两项重新组合成一个路径
  
const activePath = pathParts.join('/')
  
return activePath


}
function isActive(path) {
    return route.path.startsWith(path);
  }
function gotoAll(){router.push("/homepage/home/all")}
//  function activeIndex() {
    
//     console.log("activePath")
//     // 如果当前路径就是根路径，或者只有一项，直接使用它作为激活路径
//     if (pathParts.length === 1) {
//       return pathParts[0];
//     }
//     return activePath;
//   }

</script>

<template>
  <div class="body">
    <el-container>

      <el-aside class="aside" :width="asideWidth" ><!--侧边栏-->
        <div id="logo" >
          <img src="../assets/vip_cloud_log.png" alt="cloud_wave" width="50px">
        </div>
      <el-menu :collapse="sys.isCollapse" :collapse-transition="false" router="true" :default-active="activePath()">
        <el-menu-item index="/homepage/home" :active="isActive('/homepage/home')" @click="gotoAll()">
          
            <el-icon><House /></el-icon>
            <span slot="title">首页</span>
         
        </el-menu-item>
            
        <el-menu-item index="/homepage/recent">
          
            <el-icon><Clock /></el-icon>
            <span slot="title">最近</span>
          
        </el-menu-item>

        <el-menu-item index="/homepage/transmission">
      
            <el-icon><Switch /></el-icon>
            <span slot="title">传输</span>
         
        </el-menu-item>

        <el-menu-item index="/homepage/bin">
      
          <el-icon><Delete /></el-icon>
          <span slot="title">回收站</span>
   
        </el-menu-item>

        <el-menu-item index="/homepage/user">
          
            <el-icon><User /></el-icon>
            <span slot="title">我的</span>
          
        </el-menu-item>
      </el-menu>
      
      </el-aside>

      <el-container>
        <el-header><!--头部-->
          <div>
          <el-icon size="30px" @click="fold()"  >
            <template v-if="sys.isCollapse">
              <Expand />
            </template>
            <template v-else>
              <Fold />
            </template>  
          </el-icon> 
          </div> 
          <div class="user">
            
            <el-dropdown>
              <el-button type="primary"  class="dropdown-btn">

                <img :src="sysUser.avatar" alt="" class="user-avatar">
                <span class="user-id" v-text="sysUser.nickName"></span>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="userInfo()">个人信息</el-dropdown-item>
                  <el-dropdown-item>修改密码</el-dropdown-item>
                  <el-dropdown-item>全局设置</el-dropdown-item>
                  <el-dropdown-item @click="loginOut()">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>

            
          </div>
        </el-header>
        
          
         <router-view></router-view>
        
     
        
      </el-container>

    </el-container>
  </div>

</template>

<style lang="scss" scoped>
@import "../scss/homepage.scss";

</style>