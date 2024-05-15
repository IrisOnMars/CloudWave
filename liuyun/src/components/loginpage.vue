<script setup>
import {onMounted,onUpdated, ref} from "vue"
import { useRoute,useRouter } from 'vue-router';

const route=useRoute()
const router=useRouter()


function toregister(){
    router.push("/register")
}
function tologin(){
    router.push("/login")
}
</script>

<template>
<div class="login-body">
    <div class="box">
    <div class="forms">
        <div class="tips">
            <div  @click="tologin()">
                <span class="route-btn" :style=" {borderBottom: $route.path === '/login' ? '3px solid rgb(249,177,122)' : 'none' }" >
                    login
                </span>
            </div>
            <div  @click="toregister()">
              <span  class="route-btn" :style=" {borderBottom: $route.path === '/register' ? '3px solid rgb(249,177,122)' : 'none' }" >
                register
            </span>  
            </div>
            
        </div>

        <router-view></router-view>


    </div>
   
</div>
</div>

</template>

<style lang="scss" scoped>
@import "../scss/login.scss";

</style>