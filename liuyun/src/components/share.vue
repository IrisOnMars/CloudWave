<script setup>
import { reactive, ref ,onMounted} from "vue"
import {userStore} from "../store/userStore"
import { ElMessage } from "element-plus";
const sysUser=userStore()
const shareFormRef=ref(null)
import { useRoute,useRouter } from 'vue-router'
import request from "../utils/request"
const route=useRoute()



const shareInfo=reactive({
    accessCode:null,
    fileId:route.query.fileId,
    fileNewName:''
})

onMounted(
  async()=>{
    const {data}=await request.get(`/getShareFile/${shareInfo.fileId}`)
        console.log(data)
        if(data.code==200){
           shareInfo.fileNewName=data.data.shareFile.fileNewName
    }

  })
const validateCode =function(rule, value, callback) {
    let passwordReg=/^[a-zA-Z0-9]{5}$/  //请输入5位的数字或大小写字母       
  if (!passwordReg.test(shareInfo.accessCode )||shareInfo.accessCode==null){
    callback(new Error('请输入5位的数字或大小写字母'))
  } else {
    callback()
  }
}




const rules = reactive({
  code: [{ validator: validateCode, trigger: 'blur' }], 
})

const submitForm =function(formEl) {
  if (!formEl) return
  formEl.validate(async (valid) => {
    if (valid) {
        console.log('submit!')
        const response = await fetch('https://condor-light-suddenly.ngrok-free.app/getShare', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization':localStorage.getItem('token')
        },
        body: JSON.stringify(shareInfo)
        })
        if (!response.ok) {
            ElMessage.error("下载失败！")
            throw new Error(`HTTP error! status: ${response.status}`);
        } else {
            const url = window.URL.createObjectURL(new Blob([await response.blob()]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `${shareInfo.fileNewName}`); // 设置下载文件的名称
            document.body.appendChild(link);
            link.click();
            ElMessage.success("下载成功！")
        }
    } else {
      //ElMessage.error("提取码格式错误！")
      return false
    }
  })
}
</script>

<template>
<div class="share">
    <div class="body-content">
        <div class="logo">
            <span class="iconfont icon-pan"></span>
            <span class="name ">流云网盘</span>
        </div>
        <div class="code-panel">
            <div class="file-info">
                <div class="avatar">
                    <img :src="sysUser.avatar" alt="" class="user-avatar">
                    <span class="user-id" v-text="sysUser.nickName"></span>  
                </div>
                    <div class="share-info">
                        <div class="user-info">
                            <span class="nick-name">{{ sysUser.nickName }}</span>
                            <span class="nick-time">{{  }}</span>
                        </div>
                    <div class="file-name">分享文件:{{ shareInfo.fileNewName}}</div>
                </div>
           </div>
           <div class="code-body">
                <div class="tips">请输入提取码:</div>
                <div class="input-area">
                    <el-form
                        :rules="rules"
                        :model="shareInfo"
                        ref="shareFormRef" label-width="80px">
                        <el-form-item prop="code">
                            <el-input
                            class="input" clearable
                            v-model="shareInfo.accessCode"></el-input>
                        <el-button type="primary" @click="submitForm(shareFormRef)">提取文件</el-button>
                        </el-form-item>
                    </el-form>
                </div>
            </div>
        </div>
    </div>
</div>
</template>

<style lang="scss" scoped>
.share{
    height: calc(97.9vh);
    background: url("/src/assets/youla2k.jpg");
    background-size: 170%;
    background-position: -270px -40px;
    background-repeat: no-repeat;
    background-color:#eef2f6;
    display: flex;
    justify-content : center;
    .body-content{
        margin-top: calc(100vh/5);
        width: 500px;
        .logo {
            display: flex;
            align-items : center;
            justify-content: center;
            .icon-pan {
                font-size: 60px;
                color:#409eff;}
            .name {
                font-weight: bold;
                margin-left: 5px;
                font-size: 30px;
                color:#d7ebff;
            }
        }
        .code-panel {
            margin-top: 20px;
            background:#fff;
            border-radius : 5px;
            overflow: hidden;
            box-shadow: 0 0 7px 1px #5757574f;
            .file-info {
                padding: 10px 20px;
                background: #409eff;
                color:#fff;
                display: flex;
                align-items : center;
                .avatar {
                    display: flex;
                    margin-right: 5px;
                    align-items: center;
                    span.user-id{
                        padding-left:1vw ;
                        margin-right: 1vw;
                        font-size: 15px;
                        color: #000;   
                    }
                    img{
                        cursor: pointer; 
                        width: 40px;
                        height: 40px;
                        border-radius: 50%;
                    }
                }
                .share-info {
                    .user-info {
                        display: flex;
                        align-items: center;
                        .nick-name {
                            font-size: 15px;}
                        .share-time {
                        margin-left : 20px;
                        font-size: 12px;
                        }
                    }
                    .file-name{
                        margin-top: 10px;
                        font-size: 12px;
                    }
                }
            }
            .code-body {
                padding: 30px 20px 60px 20px;
                .tips {
                    font-weight: bold;
                }
                .input-area {
                    margin-top: 10px;
                    .input {
                        flex: 1;
                        margin-right: 10px;
                    }
                }
            }
        }
    }
}

    
    
</style>