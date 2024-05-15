<script setup>
import { reactive, ref } from "vue";
import {setStore} from "../store/setStore"
import {userStore} from "../store/userStore"
import { ElMessage } from "element-plus";
import Cropper from 'cropperjs';
import 'cropperjs/dist/cropper.css'
import request from "../utils/request";

const sysUser=userStore()
const openDialog=ref(false)
let key=0
const srcList = ref([
  sysUser.avatar
])
async function handleSuccess(response){
    console.log(response)
    if(response.code==200){
        sysUser.$patch({
                avatar:response.data.avatarUrl//将数据修改
            })
        srcList.value = [sysUser.avatar]
        console.log(srcList)
        ElMessage.success("上传成功")
        key=key+1
    }else{
        ElMessage.error("上传失败")
    }
    
}
function beforeUpload(file){
    if(!file.type.startsWith("image/")){
        ElMessage.error("请选择图片类型文件！")
        return false
    }else if(file.size>2097152){
        ElMessage.error("图片尺寸过大！")
        return false
    }else{
        return true
    }
    
}

const colors = [
  { color: '#f56c6c', percentage: 100 },
  { color: '#e6a23c', percentage: 66 },
  { color: '#5cb87a', percentage: 33 },
]

const percentage=ref((100*sysUser.useSpace/sysUser.totalSpace).toFixed(2))


function formatFileSize(sizeInBytes) {
  if (sizeInBytes === 0) return '0 B';

  const units = ['B', 'KB', 'MB', 'GB', 'TB'];
  let unitIndex = 0;

  // Find the correct unit
  while (sizeInBytes >= 1024 && unitIndex < units.length - 1) {
      sizeInBytes /= 1024;
      unitIndex++;
  }

  // Round the size to the nearest 2 decimal places
  return sizeInBytes.toFixed(2)+units[unitIndex]
}

async function refreshSpace(){
    const {data}=await request.post("/getSpace",sysUser)
    console.log(data.data)
    if(data.code==200){
        sysUser.receiveUser(data.data) 
        key=key+1
        percentage.value=(100*sysUser.useSpace/sysUser.totalSpace).toFixed(2)
        ElMessage.success("刷新成功")
       
    }else{
        ElMessage.error("刷新失败")
    }
    
}

const openChange=ref(false)
const checkUser=reactive({
    userName:sysUser.userName,
    password:'',
    msg:''
} )
const changeUser=reactive({
    userName:sysUser.userName,
    nickName:sysUser.nickName,
    password:'',
} )


const ruleFormRef = ref(null)


const validatePass =async function(rule, value, callback) {
  if (checkUser.password === '') {
    callback(new Error('请输入原密码'))
  } else {

    if (checkUser.password !== '') {
        const {data}=await request.post("/login",checkUser)
        console.log(data)
        if(data.code==200){
            callback()
        }else{
            callback(new Error('原密码错误'))
        }
    //   if (!ruleFormRef.value) return
    //   ruleFormRef.value.validateField('checkPass', () => null)
    }
    callback()
  }
}

const validatePass2 =function(rule, value, callback) {
    let passwordReg=/^[a-zA-Z0-9]{6,10}$/  //请输入6-10位的数字或大小写字母       
  if (!passwordReg.test(changeUser.password )){
    callback(new Error('请输入6-10位的数字或大小写字母'))
  } else {
    callback()
  }
}




const rules = reactive({
  pass: [{ validator: validatePass, trigger: 'blur' }], 
  pass2: [{ validator: validatePass2, trigger: 'blur' }], 
})

const submitForm =function(formEl) {
  if (!formEl) return
  formEl.validate(async (valid) => {
    if (valid) {
      console.log('submit!')
      const {data}=await request.post("/changePassword",changeUser)
        console.log(data)
        if(data.code==200){
            openChange.value=false
            sysUser.password=data.data.newUser.password
            ElMessage.success("密码修改成功！")
        }else{
            ElMessage.error("密码修改失败！")
        }
    } else {
        ElMessage.error("请正确填写内容！")
      return false
    }
  })
}
const openNickName=ref(false)
const nickName=ref(changeUser.nickName)
async function changeNickName(){
    let nickNameReg=/^[\u4e00-\u9fa5a-zA-Z0-9]{1,10}$/
    if (!nickNameReg.test(nickName.value)){
        ElMessage.error('请输入1-10位的字符')
    }else{
    changeUser.nickName=nickName
    if(changeUser.nickName!=''){
       const {data}= await request.post("/changeNickName",changeUser)
    if(data.code==200){
      console.log(data)
      ElMessage.success("重命名成功")
      openNickName.value=false
      sysUser.nickName=data.data.newUser.nickName
    }else{
        openNickName.value=false
      ElMessage.error(`重命名失败`)} 
    }
}}

</script>

<template>
<div class="main">
    <!-- {{sysUser.token}} -->
    <div class="m1">
        <div class="avatar">
            <el-avatar 
            :size="100" 
            :src="sysUser.avatar"
            style="cursor: pointer;"
            @click="openDialog = true"/>

            <span class="username">{{ sysUser.nickName }}</span>
            <el-divider style="width: 80%;" />
        </div>


        <div class="userdiv" style="margin-top:150px">
            <el-progress type="dashboard" :percentage="percentage" :color="colors" />
            <div style=" display: flex;
                        flex-direction: row; 
                        align-items: center;
                        color:#7f7f7f"
                        :key="key">
                <el-icon style="cursor:pointer;" @click="refreshSpace" ><Refresh /></el-icon> 
                <span style="color:#7f7f7f">空间使用情况：{{ formatFileSize(sysUser.useSpace) }}/{{ formatFileSize(sysUser.totalSpace) }}</span>
            </div>
           
        </div>
        
        <el-dialog  draggable  align-center  v-model="openDialog" 
            title="上传头像" width="600" >
            <div class="dialog">
                <div class="userdiv">
                    <el-image
                    :key="key"
                    style="width: 200px; height: 200px ; border-radius: 12px; border: 1px solid #666666;"
                    :src="sysUser.avatar"
                    hide-on-click-modal="true"
                    :zoom-rate="1.2"
                    :max-scale="7"
                    :min-scale="0.2"
                    :preview-src-list="srcList"
                    fit="cover"
                    />
                    <span style="margin-top:20px">当前头像</span>
                    
                </div>
                <el-divider direction="vertical"  style="height: 80%;"/>
                <div  class="userdiv">
                    <el-upload
                    class="upload-avatar"
                    action="https://condor-light-suddenly.ngrok-free.app/avatar/upload"
                    :data="sysUser"
                    :before-upload="beforeUpload"
                    :on-success="handleSuccess"
                    :show-file-list="false"
                >
                    <el-button type="primary">选择本地图片</el-button>
                    <template #tip>
                    <div class="el-upload__tip" style="width:160px">
                        请选择图片上传：大小200 * 200像素,支持JPG、PNG等格式，图片需小于2M
                    </div>
                   
                    </template>
                </el-upload>
                </div>
                
            </div>
            <!-- <template #footer>
                <div class="dialog-footer">
                    <el-button @click="openDialog = false">取消</el-button>
                    <el-button type="primary" @click="openDialog = false">
                    确定</el-button>
                </div>
            </template> -->
        </el-dialog>


    </div>
    <div >
       <div class="m2">
            <span class="vip">VIP等级：0</span>
            <el-progress
            style="width: 70%;margin:0 0 10px 60px; "
                :text-inside="true"
                :stroke-width="22"
                :percentage="80"
                status="warning"
                />
        </div>
        <div class="m3">
            <h1>用户信息</h1>
            <el-form :model="sysUser" label-width="auto" style="width:350px;max-width: 600px">
                <el-form-item label="用户名">
                    <el-input disabled v-model="sysUser.userName" />
                </el-form-item>
                <el-form-item label="昵称">
                    <el-input disabled v-model="sysUser.nickName" />
                </el-form-item>
                <el-form-item label="邮箱">
                    <el-input disabled v-model="sysUser.email" />
                </el-form-item>
                <el-form-item label="密码" >
                    <el-input type="password"  disabled />
                </el-form-item>
            </el-form>
            <div style="display:flex;">
                <el-button @click="openNickName=true" type="primary" style="height: 38px;"> 修改昵称
                </el-button>
                <el-button @click="openChange=true" type="primary" style="height: 38px;"> 修改密码
                </el-button>
            </div>
            

            <el-dialog  draggable  align-center  v-model="openChange" 
            title="修改密码" width="500"  >
            <div class="userdiv">
                <el-form :model="sysUser"
                 ref="ruleFormRef" 
                 status-icon
                :rules="rules" 
                label-width="auto" 
                style="max-width: 600;">
                <el-form-item label="当前密码" prop="pass">
                    <el-input type="password" v-model="checkUser.password"  />
                </el-form-item>
                <el-form-item label="新密码"  prop="pass2" >
                    <el-input type="password" v-model="changeUser.password"  />
                </el-form-item>
                <el-form-item style="display:flex ;float:right">
                    <el-button type="primary" @click="submitForm(ruleFormRef)">
                        修改
                    </el-button>
                    <el-button @click="openChange=false">取消</el-button>
                    </el-form-item>
                </el-form>
            </div>
            <!-- <template #footer>
                <div class="dialog-footer">
                    <el-button @click="openChange = false">取消</el-button>
                    <el-button type="primary" @click="changePassword">
                    确定</el-button>
                </div>
            </template> -->
        </el-dialog>

        <el-dialog class="dialog" v-model="openNickName" title="重命名昵称" draggable top="35vh" width="500" >
            <el-form >
            <el-form-item label="新的昵称" label-width="140px" style="margin-top:15px" >
                <el-input v-model="nickName"
                clearable
                style="width:260px" />
            </el-form-item>
            </el-form>
            <template #footer>
            <div class="dialog-footer"  >
                <el-button @click="openNickName = false">取消</el-button>
                <el-button type="primary" @click="changeNickName()">
                确认</el-button>
            </div>
            </template>
        </el-dialog>
        </div> 
    </div>
    

</div>
   
</template>

<style lang="scss" scoped>
.userdiv{
    display: flex;
    flex-direction: column; 
    margin:auto;
    align-items: center;
    justify-content:center
    
}
img {
  display: block;

  /* This rule is very important, please don't ignore this */
  max-width: 100%;
}
.dialog{
    height: 300px;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;
}
.main{
    height: 100%;
    background-color: #f3f5fb;
    display: flex;
}
.avatar{
    width: 100%;
    height: 270px;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
}
.username{
    margin-top: 20px;
    font-size: 28px;
    font-weight: 600;
}
.m1{
    background-color: #fff;
    float: left;
    height: 100%;
    width: 33vw;
    box-shadow: 0 2px 20px 0 rgba(0, 0, 0, 0.05);   
}
.m2{
    display: flex;
    justify-content: center;
    flex-direction: column;
    margin: 15px;
    height: 20vh;
    width: 60vw;
    border-radius: 12px;
    background-color: #fff;
    box-shadow: 0 2px 20px 0 rgba(0, 0, 0, 0.05);   
    z-index: 1;
    .vip{
        margin:0 0 10px 60px;
        font-weight: 600;
    }
}
.m3{
    display: flex;
    justify-content: center;
    flex-direction: column;
    align-items: center;
    margin: 20px;
    height: 63vh;
    width: 60vw;
    border-radius: 12px;
    background-color: #fff;
    box-shadow: 0 2px 20px 0 rgba(0, 0, 0, 0.05);   
    z-index: 1;
}

</style>