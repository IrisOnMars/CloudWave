<script setup>
import { fileStore } from '../store/fileStore'
import { userStore } from '../store/userStore'
import myicon from '/src/components/myicon.vue'
import request from "../utils/request"
import {onMounted, reactive, ref} from 'vue'
import { useRoute,useRouter } from 'vue-router'
import { ElMessage, ElMessageBox,ElNotification } from 'element-plus'
import { iconStore } from '/src/store/iconStore'
const sysUser=userStore()
const props=defineProps(['category'])
const icon=iconStore()
const router=useRouter()
const route=useRoute()
const fileList=fileStore()
const formLabelWidth="120px"
const folder=reactive({
  userName:sysUser.userName,
  fileCategory:props.category})
onMounted(
  async()=>{
    
   await updateData(folder)

   console.log(folder)
  
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
const list=reactive({file:[]})

async function moveToBin(uid){
  list.file=[{fileId:uid}]
  
  const {data}= await request.post("/file/moveToBin",list.file)
  console.log(data)
  const total=data.data.total
  const count=data.data.count
  if(data.code==200){
        
    updateData(folder)
        ElNotification({
            title: '回收成功',
            type:'success',
            message: `检测到${total}个文件，实际移动${count}个至回收站`,
            position: 'bottom-left',
        })

        
    }else{
        ElNotification({
            title: '出差错了',
            type:'warning',
            message: `检测到${total}个文件，实际移动${count}个至回收站`,
            position: 'bottom-left',
        })
    }

}



function formattedCreateTime(data){
  const createTime = new Date(data.createTime);    
    // 格式化日期时间为"YYYY-MM-DD HH:mm:ss"的形式
  const formattedCreateTime = createTime.toLocaleString()
    // 将格式化后的字符串赋值回createTime字段
  return formattedCreateTime
}

async function updateData(folder){
  console.log("folder")
  console.log(folder)
  const {data}= await request.post("/file/getFilesByCategory",folder)
  if(data.code==200){

  fileList.receiveFiles(data.data.fileList)
  console.log(data)
  console.log('数据已更新'); 
  ElMessage.success('数据已更新'); 
  }
  else{
 
  }
}

const selectFileIdList=ref([])
function handleSelect(selection, row) {
  //row.createTime=null
  selectFileIdList.value=[]  
  selection.forEach((item)=>{
    selectFileIdList.value.push(item.fileId)
  }) 
  
}
function handleSelectAll(selection) {
  selection.forEach((item)=>{
    //item.createTime=null
  }) 
  selectFileIdList.value=selection
  
}

function printSelected(){
  console.log(selectFileIdList);
}

function handleSelectionChange(val) {
  selectFileIdList.value = val;
  console.log(val)
}
const renameDialog=ref(false)

const newNameFile=reactive({
    userName:sysUser.userName,
    fileName:'',
    fileId:0
  })
function openRenameDialog(row){
  
  newNameFile.fileName=row.fileNewName,
  newNameFile.fileId=row.fileId
  renameDialog.value=true
}

async function setName(){
  const {data}= await request.post("/file/setName",newNameFile)
    if(data.code==200){
      console.log(data)
      ElMessage.success("重命名成功")
      renameDialog.value=false
      if(route.query.currentId!=undefined){
        folder.fileId=route.query.currentId
      }
      updateData(folder)
    }else if(data.code==507){
      ElMessage.error(`已存在名为 ${newNameFile.fileName} 的文件了！`)
    }
}

function goToUpload(){
  router.push({path:'/homepage/transmission',query:{autoClick: true}})
}
</script>

<template>
<div>
   <div class="btnBar">
    <el-button class="upload" type="primary" @click="goToUpload">
        <el-icon class="UploadFilled" >
            <UploadFilled />
        </el-icon>
        <span>上传文件</span>
    </el-button>
      <DelBtn
    @data-updated="updateData" 
    :selectLength="selectFileIdList.length"
    :selectedFiles="selectFileIdList"
    ></DelBtn>
   </div>
   

   <el-table  
  border
  :data="fileList.fileData" 
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
        <div style="margin-left:20px; width:200px;display:flex;align-items: center;">
          <el-text class="userNameSpan" truncated :title="scope.row.fileNewName" @dblclick="spanDoubleClick">
            {{ scope.row.fileNewName }}
          </el-text>
          <!-- <span class="userNameSpan" 
          style="cursor: pointer;" 
          @dblclick="spanDoubleClick">{{ scope.row.fileNewName }}</span> -->
        </div> 
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

  <el-dialog class="dialog" v-model="renameDialog" title="文件重命名" draggable top="35vh" width="500" >
    <el-form >
    <el-form-item label="新的名称" :label-width="formLabelWidth" style="margin-top:15px" >
        <el-input v-model="newNameFile.fileName"
        clearable
        style="width:260px" />
    </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer"  >
          <el-button @click="renameDialog = false">取消</el-button>
          <el-button type="primary" @click="setName()">
          确认</el-button>
      </div>
    </template>
  </el-dialog>
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