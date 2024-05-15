<script setup>
import { Timer } from '@element-plus/icons-vue'
import { fileStore } from '../store/fileStore'
import { userStore } from '../store/userStore'
import myicon from '/src/components/myicon.vue'
import request from "../utils/request"
import {onMounted, reactive, ref} from 'vue'
import { useRoute,useRouter } from 'vue-router'
import { ElMessage, ElMessageBox ,ElNotification} from 'element-plus'
import { iconStore } from '/src/store/iconStore'
const formLabelWidth="120px"
const icon=iconStore()
const router=useRouter()
const route=useRoute()
const sysUser=userStore()
const fileList=fileStore()
const user=reactive({
  userName:sysUser.userName
})

onMounted(
  async()=>{
   await updateData()
  
  }
)



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

function formattedCreateTime(data){
  const createTime = new Date(data.createTime);    
    // 格式化日期时间为"YYYY-MM-DD HH:mm:ss"的形式
  const formattedCreateTime = createTime.toLocaleString()
    // 将格式化后的字符串赋值回createTime字段
  return formattedCreateTime
}

async function updateData(){
  
  const {data}= await request.post("/file/getRecent",user)
  if(data.code==200){

  fileList.receiveFiles(data.data.fileList)
  console.log('数据已更新'); 
  ElMessage.success('数据已更新'); 
  }
  else{
 
  }
}

function goToUpload(){
  router.push({path:'/homepage/transmission',query:{autoClick: true}})
}
</script>

<template>
<div>

    <el-button class="upload" type="primary" @click="goToUpload" style="margin:10px;float:left">
          <el-icon class="UploadFilled" >
              <UploadFilled />
          </el-icon>
          <span>上传文件</span>
    </el-button>
    <div class="mx">
      <span  >这里会显示最近的20个文件</span>
    </div>
    
    
  <el-table  
  border
  :data="fileList.fileData" 
  @cell-dblclick="clickFolder"
  @cell-mouse-enter="showOptions"
  @cell-mouse-leave="hideOptions" 
  @select="handleSelect"
  @select-all="handleSelectAll"
  @selection-change="handleSelectionChange"
  height="540px"
  cell-class-name="rowClass"
  style="width: 100% ">
    <el-table-column type="selection" width="55" />
    <el-table-column label="文件名" width="340px" >
      <template #default="scope">
        
        <div class="fileNameColumn" >
      
          <!-- <img :src="getFileIcon(scope.row.fileType)" width="30px"  /> -->
          <myicon style="cursor: pointer;" :fileType="scope.row.fileType" width="30px"/> 
          <div style="margin-left:20px; width:180px">
            <span class="userNameSpan" 
            style="cursor: pointer;" 
            @dblclick="spanDoubleClick">{{ scope.row.fileNewName }}</span>
          </div>
          <!-- {{scope.row.fileId}} -->
          
        </div> 
      </template>
    </el-table-column>
    <el-table-column label="操作" width="400" 
      @mouseenter="showOptions(scope.row)"
      @mouseleave="hideOptions(scope.row)" >
      <template #default="scope" >
        <div>
          <div v-show="scope.row.showOptions">
              <div style="margin:0px 0px 0px 160px;font-size: 24px">
                <el-icon title="重命名" @click="openRenameDialog(scope.row)"><Edit /></el-icon>     
                <el-icon title="下载" @click="download()"><Download /></el-icon>
                <el-icon title="分享" @click="share(scope.row.fileId)"><Share /></el-icon>
                <el-icon title="移至回收站" @click="moveToBin(scope.row.fileId)"><DeleteFilled /></el-icon>
              </div>
          </div>         
        </div> 
      </template>
    </el-table-column>
    <el-table-column label="修改时间" width="220">
      <template #default="scope">
        <span style="margin-left: 10px">{{ formattedCreateTime(scope.row) }}</span>
      </template>
    </el-table-column>
    <el-table-column label="大小">
      <template #default="scope">
        <span v-if="!scope.row.isFolder" style="margin-left: 10px">{{formatFileSize(scope.row.fileSize)  }}</span>
      </template>
    </el-table-column>
  </el-table>
</div>
</template>


<style lang="scss" scoped>
.upload{
   height: 38px;
   
   margin-right: 10px;
}
.btnBar{
  display:flex;
  margin-bottom: 10px;
}
.rowClass{
  padding: 0;
}
.fileNameColumn{
  display: flex ;
  align-items: center;
  height: 40px;
  &:hover{
    color: #1197fe;
  }
}
.mx{
  color: #67c23a;
  
  padding:18px 0 0 160px ;
  align-items: center;
}
.userNameSpan{
  -moz-user-select : none;
  user-select:none
}
.el-icon{
  font-size:20px;
  margin-left: 10px;
}

.el-icon:hover{
  color: #1197fe;
  cursor: pointer;
}

.UploadFilled{
  font-size: 16px;
  margin: 0;
}
.UploadFilled:hover{
  color: #fff;
  
}
</style>