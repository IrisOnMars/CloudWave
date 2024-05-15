<script setup>
import { useRoute,useRouter } from 'vue-router'
import {ref,reactive, onMounted} from 'vue'
import request from "../utils/request"
import {setStore} from "../store/setStore"
import {userStore} from "../store/userStore"

const sys=setStore()
const sysUser=userStore()
const router=useRouter()
const route=useRoute()
const loginUser=reactive({
    userName:"",
    password:"",
    userNameMsg:"",
    passwordMsg:"",
    
})



async function login(){            
        const {data}=await request.post("/login",loginUser)
        console.log(data)
        if(data.code==200){
            sysUser.avatar="/src/assets/user.png"
           sysUser.receiveUser(data.data)
           
           sysUser.$patch({
                password:loginUser.password//将数据修改
            })
            localStorage.setItem("token", data.data.token);
            const redirect = route.query.redirect;

          // 如果有redirect参数，则重定向到该页面
          if (redirect) {
            router.push(redirect);
          } else {
           // 如果没有redirect参数，重定向到默认页面（例如首页）
            router.push('/homepage/home/all');
          }
           

        }else if(data.code==501){
            alert("用户名不存在！")
        }else if(data.code==503){
            alert("密码错误！")
        } else{
            alert("未知错误！")
        }        
    }
onMounted(()=>{
    if(sys.isRemember){ 
        loginUser.userName=sysUser.userName
        loginUser.password=sysUser.password  
        
    }
})
// function readLocalUser(){
//     if(sys.isRemember==true){
        
//         loginUser.userName=sysUser.userName
//         loginUser.password=sysUser.password  
        
//     }
// }
// readLocalUser()
function updatepwd(){
    router.push("/forgot_pwd")
}


</script>

<template>
        <div class="login">
            <div class="form-title">
                <h1>Hello,Cloudwave</h1>
            </div>
            <div class="form">
                <div class="username input-item" >
                    <input type="text" class="ips" v-model="loginUser.userName">
                    <span v-text="loginUser.userNameMsg"></span>
                </div>
                <div class="password input-item" >
                    <input type="password" class="ips" v-model="loginUser.password">
                    <span v-text="loginUser.passwordMsg"></span>
                </div>
                <div class="other-select">
                    <div class="rem-pwd">
                        <input type="checkbox" id="check" v-model="sys.isRemember">
                        <label for="check" class="rem-pwd-tips">remember</label>
                    </div>
                    <span class="forgot-pwd-btn" @click="updatepwd()">forgot password?</span>
                </div>
                <button class="btn" @click="login()">login</button>
                <div class="other-login">
                    <div class="other-login-tips">
                        <span>——————— Others ———————</span>
                    </div>
                    <div class="login-img">
                        <img src="../assets/QQ.png" alt="">
                        <img src="../assets/weichat.png" alt="">
                    </div>
                </div>
            </div>
        </div>
</template>

<style lang="scss" scoped>
@import "../scss/login.scss";

</style>