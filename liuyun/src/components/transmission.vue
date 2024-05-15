<script setup>
import { fileStore } from '../store/fileStore'
import { userStore } from '../store/userStore'
import { uploadStore } from '../store/uploadStore'
import { iconStore } from '../store/iconStore'
import myicon from '/src/components/myicon.vue'
import request from "../utils/request"
import {computed, onMounted, reactive, ref, toRefs} from 'vue'
import { useRoute,useRouter } from 'vue-router'
const router=useRouter()
const route=useRoute()

const sysUser=userStore()
const icon=iconStore()

const uploads=uploadStore()
const fileList=uploads.uploadList

import { ElMessage, ElMessageBox } from 'element-plus'





onMounted(()=>{
  const autoClick = route.query.autoClick;

  const btn = document.getElementById("uploadBtn")
  if (autoClick === "true") {
      // 如果autoClick为true，则自动点击按钮
        btn.click()
    }
})
const handleProgress=(progressEvent, file, )=> {//文件上传时的钩子
    const index = fileList.findIndex(item => item.name === file.name);
    if (index !== -1) {
      // 将进度更新为上传事件中的进度值
      fileList[index].percentage= Math.round(progressEvent.percent * 100) / 100 
       
      }
      
      console.log(`当前进度：${progressEvent.percent}%`);
    }


// function success(response) {
//     console.log(response)
//       // 假设后端返回的是一个对象，并且包含data属性
//       if (response && response.data) {
//         myData.value = response.data; // 将返回的数据赋值给myData
//       } else {
//         myData.value = 'No data received'; // 如果未收到数据，则赋予默认值
//       }
      
// }

const fileMsg=reactive({
    userName:sysUser.userName,
    filePid:route.query.currentId||0,
    fileSize:120,
  })

const beforeUpload=async(file)=>{
      // 在文件上传之前做些什么，比如限制文件类型、大小等
      // 这里可以将文件添加到 fileList 中
       // 添加文件大小（以字节为单位）
       const currentId=route.query.currentId||0
      fileMsg.filePid=currentId
      fileMsg.fileSize=file.size
      fileMsg.fileCategory=getMyFileCategory(file.type)
      fileMsg.fileNewName=file.name
      console.log(mimeTypeToCategory[file.type]==icon.ppt)
      console.log(file.type)
      console.log("fileMsg.fileCategory"+fileMsg.fileCategory)
      const index = fileList.findIndex(item => item.name === file.name);

      const {data}= await request.post("/file/checkCopy",fileMsg)
     if (data.code!=200) {
    await   ElMessageBox.confirm(
      '已经有一个同名文件了，确定要继续上传吗？',
      '警告',
    {
      confirmButtonText: '继续',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    uploads.addFile(file)
    
    ElMessage({
      type: 'success',
      message: '继续上传',

    })
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '取消上传',
      
    })
  })
      
    }else{
      uploads.addFile(file)
      
      console.log("'file'+fileList")
    }
      
    
    // 如果找到同名文件，则提示用户重命名
    
  //   const newName = prompt(`文件名“${file.name}”已存在，请输入新的文件名：`);
  //   if (newName && newName.trim() !== '') {
  //     // 用户输入了新的文件名，更新文件对象并继续上传
  //     console.log('准备重命名', file.name);
  //     const newfn=newName.trim()
  //     const copyFile = new File([file], newfn,{type:file.type})
  //     console.log("copyFile",copyFile.name)
  //      copyFile.uid = file.uid; // new File 没有uid属性，会导致组件底层报错，这里手动加上
      
  //     console.log("原始File",file)
      
  
  //     uploads.addFile(copyFile);
  //     console.log('文件已重命名并添加到上传队列：', copyFile);
  //     return copyFile
  //      // 允许上传
  //   } else {
  //     // 用户点击了取消或者输入了空字符串，阻止上传
  //     console.log('用户取消了重命名操作，文件上传被阻止。');
  //     return false; // 阻止上传
  //   }
  // }else{
    // this.uploads.addFile({
      //   name: file.name,
      //   percentage: 0, // 初始进度为 0
      //   size: size,
      //   type: type,
      //   url: '', // 可以存储上传成功后的文件 URL
      //   status:null
      // });
   
      
    // 返回 false 以阻止自动上传
  
     
      
}



const handleSuccess=(response, file,)=> {
      // 文件上传成功后的处理
      const index = fileList.findIndex(item => item.uid === file.uid);
      if (index !== -1) {
        fileList[index].percentage=100  // 设置进度为 100%
        fileList[index].status="success"
        fileList[index].url=response.data // 可以设置文件 URL
        }
    }

const mimeTypeToCategory = {
  //文档文件
  'application/vnd.ms-excel':icon.excel,//xls
  'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet': icon.excel,//xlsx
  'application/vnd.openxmlformats-officedocument.wordprocessingml.document':icon.word,//docx
  'application/msword': icon.word,//doc
  'application/pdf': icon.pdf,
  'application/vnd.ms-powerpoint':icon.ppt,
  'application/vnd.openxmlformats-officedocument.presentationml.presentation':icon.ppt,//pptx
  //压缩包文件
  'application/zip':icon.zip,
  'application/x-zip-compressed':icon.zip,
  'application/x-rar-compressed':icon.zip,//rar
  'application/x-gzip':icon.zip,//gz
  'application/x-bzip2':icon.zip,//bz2

  // 添加其他需要的MIME类型与文件大类的映射
}

function getFileCategory(mimeType) {
  return mimeTypeToCategory[mimeType] || icon.default
}
function  getFileIcon(fileType) {
      if (fileType.startsWith('image/')) {
        return icon.image // 图片文件类型图标
      } else if (fileType.startsWith('video/')) {
        return icon.video // 视频文件类型图标
      } else if (fileType.startsWith('audio/')) {
        return icon.audio // 音频文件类型图标
      } else if (fileType.startsWith('text/')) {
        return icon.text // text 文件类型图标
      }
      else {
        return getFileCategory(fileType) // 默认文件类型图标
      }
    }   

    function  getMyFileCategory(fileType) {
      if (fileType.startsWith('image/')) {
        return 'image' // 图片文件类型
      } else if (fileType.startsWith('video/')) {
        return 'video' // 视频文件类型
      } else if (fileType.startsWith('audio/')) {
        return 'audio' // 音频文件类型
      } else if(mimeTypeToCategory[fileType]==icon.zip){
        return 'zip' //zip文件类型(
      }else if (fileType.startsWith('text/')||
      mimeTypeToCategory[fileType]==icon.pdf||
      mimeTypeToCategory[fileType]==icon.ppt||
      mimeTypeToCategory[fileType]==icon.word||
      mimeTypeToCategory[fileType]==icon.excel) {
        return 'document' // text 文件类型
      }
      else {
        return 'others' // 默认文件类型
      }
    }  


 function formatFileSize(sizeInBytes) {
    if (sizeInBytes === 0) return '0 Bytes';

    const units = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
    let unitIndex = 0;

    // Find the correct unit
    while (sizeInBytes >= 1024 && unitIndex < units.length - 1) {
        sizeInBytes /= 1024;
        unitIndex++;
    }

    // Round the size to the nearest 2 decimal places
    return sizeInBytes.toFixed(2)+units[unitIndex]
}

 // 鼠标移入时显示选项
 function showOptions(row) {
      // 在 row 对象中添加一个名为 showOptions 的属性，用于控制选项的显示
      row.showOptions=true
    }
    // 鼠标移出时隐藏选项
function  hideOptions(row) {
      // 在 row 对象中隐藏选项
      row.showOptions=false
    }

function moveToBin(uid){
  const success=uploads.removeFile(uid)

  if (success) {
    // 成功删除，弹出提示框
    ElMessage.success('删除成功');
  } else {
    // 删除失败，弹出提示框
    ElMessage.error('删除失败');
  }
}
const reversedData= computed(() => {
  return [...uploads.uploadList].reverse()
})

</script>

<template>
<div>
    <el-upload
    
    class="upload-demo"
    action="https://condor-light-suddenly.ngrok-free.app/file/upload"
    :data="fileMsg"
    :on-success="handleSuccess"
    :on-progress="handleProgress"
    multiple
    :show-file-list="false"
    :before-upload="beforeUpload"
    :file-list="fileList"
    :submit="fileList"
  >
    <el-button id="uploadBtn" type="primary" style="height: 38px;"> 
      <el-icon class="UploadFilled" size="25px">
        <UploadFilled />
      </el-icon>上传文件
    </el-button>
   
  </el-upload>  


    <!-- <div  v-for="(file, index) in fileList" :key="index" class="file-progress">
      <img :src="getFileIcon(file.type)" width="30px" alt="File Type Icon" />
      <span>{{ file.name }}</span>
      <span>{{ formatFileSize(file.size)}}</span>
      
      <el-progress :percentage="file.percentage" :status="file.status"></el-progress>
    </div> -->

  <el-table   
  :data="reversedData" 
  height="500px"
  style="width: 100% ">
    <el-table-column label="文件名" width="550px">
      <template #default="scope">
        <div style="display: flex; align-items: center"
        @mouseenter="showOptions(scope.row)"
        @mouseleave="hideOptions(scope.row)">
          <!-- <img :src="getFileIcon(scope.row.type)" width="30px"  />  -->
          <myicon :fileType="scope.row.type" width="30px"/> 
          <div style="margin-left:10px;width:180px">
            <span >{{ scope.row.name }}</span>
          </div>
          
          <div  v-show="scope.row.showOptions">
              <div style="margin:0px 0px 0px 140px;font-size: 24px">
                <el-icon title="下载" @click="download()"><Download /></el-icon>
                <el-icon title="分享" @click="share()"><Share /></el-icon>
                <el-icon title="移至回收站" @click="moveToBin(scope.row.uid)"><DeleteFilled /></el-icon>
              </div>
          </div>
        </div>
      </template>
    </el-table-column>
   
    <el-table-column label="进度" width="300px">
      
      <template #default="scope">
        <el-progress :percentage="scope.row.percentage" :status="scope.row.status"></el-progress>
        <!-- <span style="margin-left: 10px">{{ scope.row.percentage }}</span> -->
      </template>
    </el-table-column>
    <el-table-column label="大小">
      <template #default="scope">
        <span style="margin-left: 10px">{{ formatFileSize(scope.row.size) }}</span>
      </template>
    </el-table-column>
  </el-table>
</div>
</template>

<style scoped>
*{
    margin: 3px;
}
.el-icon{
  margin-left: 10px;
}

.el-icon:hover{
  color: #1197fe;
  cursor: pointer;
}
.UploadFilled{
  margin-left:0
}
.UploadFilled:hover{
  color: #fff;
  
}
</style>