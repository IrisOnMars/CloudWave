<script setup>
import { useRoute,useRouter } from 'vue-router'
import {ref,reactive} from 'vue'
import request from "../utils/request"
const router=useRouter()
const registerUser=reactive({
    userName:"",
    email:"",
    password:"",
    confirm_password:"",
    
    userNameMsg:"",
    emailMsg:"",
    passwordMsg:"",
    confirm_passwordMsg:"",

})
// const check=reactive({
//     username:2,
//     password:"",
//     confirm_password:"",
//     email:"",

// })

// const passwordInput=document.getElementById("password-input")
// const emailInput=document.getElementById("email-input")
// const confirmPasswordInput=document.getElementById("confirm-password-input")

 //请输入3-10位的数字或大小写字母


// const isFocus=false
// function usernameFocus(){
//     isFocus=true
// }
const registerStyle=reactive({
    borderLeft:'3px solid rgb(2,177,122)',
    padding:'20px 5px 1px 17.5px'
})   
const usernameStyle=ref('')
const usernameStyle2=ref('')
const emailStyle=ref('')
const emailStyle2=ref('')
const passwordStyle=ref('')
const passwordStyle2=ref('')

const confirmPasswordStyle=ref('')
const confirmPasswordStyle2=ref('')
async function checkUsername(){
       const usernameReg=/^[a-zA-Z0-9]{3,10}$/ 
        // if(check.username>=-100){
        //     console.log("hhh")
        //     check.username=check.username-1
        //     return false
        // }
        if(!usernameReg.test(registerUser.userName)){
            registerUser.userNameMsg="·3-10位的字母或数字"
            usernameStyle.value=''
            usernameStyle2.value=''
            return false
        }
        const {data}=await request.post(`https://condor-light-suddenly.ngrok-free.app/checkUserExist`,registerUser)
        console.log(data)    
        if(data.code!=200){
                registerUser.userNameMsg="·用户名已存在"
                return false
        }
        usernameStyle.value=registerStyle.borderLeft
        usernameStyle2.value=registerStyle.padding
        registerUser.userNameMsg=""
       
        return true
    }

async function checkEmail() {
    const emailReg = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if(!emailReg.test(registerUser.email)){
            registerUser.emailMsg="·邮箱格式错误"
            emailStyle.value=''
            emailStyle2.value=''
            return false
        }
        emailStyle.value=registerStyle.borderLeft
        emailStyle2.value=registerStyle.padding
        registerUser.emailMsg=""
        
        return true
}


function checkPassword(){
    let passwordReg=/^[a-zA-Z0-9]{6,10}$/  //请输入6-10位的数字或大小写字母
    if(!passwordReg.test(registerUser.password)){
        registerUser.passwordMsg="·6-10位数字或字母"
        passwordStyle.value=''
        passwordStyle2.value=''
        return false
    }
    registerUser.passwordMsg=""
    passwordStyle.value=registerStyle.borderLeft
    passwordStyle2.value=registerStyle.padding
    return true
}

function checkConfirmPassword(){
            let passwordReg=/^[a-zA-Z0-9]{6,10}$/  //请输入6-10位的数字或大小写字母
            let flag=checkPassword()
           
            if(!passwordReg.test(registerUser.confirm_password)){
                registerUser.confirm_passwordMsg="·6-10位数字或字母"
                confirmPasswordStyle.value=''
                confirmPasswordStyle2.value=''
                return false
            }else if(flag){
                if(registerUser.confirm_password!=registerUser.password){
                    registerUser.confirm_passwordMsg="·两次密码不一致"  
                    confirmPasswordStyle.value=''
                    confirmPasswordStyle2.value=''
                    return false
                }else{
                    registerUser.confirm_passwordMsg=""
                    //registerUser.confirm_passwordMsg="✔"
                    confirmPasswordStyle.value=registerStyle.borderLeft
                    confirmPasswordStyle2.value=registerStyle.padding
                } 
            }
            
            return true
        }


async function register(){     
    const check1=await checkUsername()
    const check2= checkEmail()
    const check3= checkPassword()
    const check4= checkConfirmPassword()

    if(check1&&check2&&check3&&check4){
        const {data}=await request.post("/register",registerUser) 
        console.log(data)
        if(data.code==200){
            alert("注册成功，快去登录吧！")
            router.push("/login")
        }else{
            alert("未知错误，注册失败！")
        }
    }
    else{
        alert("用户名或密码输入不合法！")
    }

        

}


</script>

<template>
 <div class="register">
            <div class="form-title">
                <h1>Hello,Cloudwave</h1>
            </div>
            <div class="form">
                <div class="username input-item" >
                    <input id="username-input" type="text" class="ips" v-model="registerUser.userName"  @input="checkUsername()" 
                    
                    :style="{ borderLeft:  usernameStyle, padding: usernameStyle2}" >
                    <span  v-text="registerUser.userNameMsg"></span>
                </div>
                <div class="email input-item">
                    <input id="email-input"  type="email" class="ips" v-model="registerUser.email" @input="checkEmail()" 
                    :style="{ borderLeft: emailStyle, padding: emailStyle2}">
                    <span v-text="registerUser.emailMsg"></span>
                </div>
                <div class="password input-item">
                    <input id="password-input" type="password" class="ips" v-model="registerUser.password" @input="checkPassword()" 
                    :style="{ borderLeft: passwordStyle, padding: passwordStyle2}">
                    <span v-text="registerUser.passwordMsg"></span>
                </div>
                <div class="conform-password input-item">
                    <input id="conform-password-input" type="password" class="ips" v-model="registerUser.confirm_password" @input="checkConfirmPassword()" 
                    :style="{ borderLeft:confirmPasswordStyle, padding: confirmPasswordStyle2}">
                    <span v-text="registerUser.confirm_passwordMsg"></span>
                </div>
                
               
                <button class="btn" @click="register()">register</button>
                
            </div>
        </div>
</template>

<style lang="scss" scoped>
@import "../scss/login.scss";
.btn{
    margin-top: 20px;
}
.input-item{
    margin: 15px 0;
}
</style>