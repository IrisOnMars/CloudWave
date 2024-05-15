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
const icon=iconStore()
const router=useRouter()
const route=useRoute()
const fileList=fileStore()
const openDialog=ref(false)
const refreshDialog=ref(false)
const folder=reactive({
  userName:sysUser.userName,
  fileId:0})
onMounted(
  async()=>{    
   await updateData(folder)
   console.log(folder)
  
  }
)


 // 鼠标移入时显示选项
function showOptions(row) { 
      row.showOptions=true
}
//鼠标移出时隐藏选项    
function  hideOptions(row) { 
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

async function updateData(folder){
  console.log("folder")
  console.log(folder)
  const {data}= await request.post("/file/getBin",folder)
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


const refreshList=reactive({file:[]})
  
async function refresh(id){
    refreshDialog.value = false
    refreshList.file=selectFileIdList.value.length>0?selectFileIdList.value:[{fileId:id}]
    console.log(refreshList.file)
    const {data}= await request.post("/file/refresh", refreshList.file)
    const total=data.data.total
    const count=data.data.count
    if(data.code==200){
        ElNotification({
            title: '恢复成功',
            type:'success',
            message: `检测到${total}个文件，实际恢复${count}个至根目录`,
            position: 'bottom-left',
        })
        updateData(folder)
    }else{
        ElNotification({
            title: '出差错了',
            type:'warning',
            message: `检测到${total}个文件，实际恢复${count}个至根目录`,
            position: 'bottom-left',
        })
    }
}
function openDelDialog(id){
    openDialog.value=true
    delList.files=[{fileId:id}]
}
const delList=reactive({files:[]})

async function delForever(){
    openDialog.value=false
    console.log(delList.files)
    const {data}= await request.post("/file/delForever", selectFileIdList.value.length>0?selectFileIdList.value:delList.files)
    const total=data.data.total
    const count=data.data.count
    if(data.code==200){
        ElNotification({
            title: '删除成功',
            type:'success',
            message: `检测到${total}个文件，实际删除${count}个文件`,
            position: 'bottom-left',
        })
        updateData(folder)
    }else{
        ElNotification({
            title: '出差错了',
            type:'warning',
            message: `检测到${total}个文件，实际删除${count}个文件`,
            position: 'bottom-left',
        })
    }
}
</script>

<template>
<div>
    <el-button type="danger" style="height:38px ;margin:10px 0px 10px 10px" :disabled="selectFileIdList.length==0" @click="openDialog = true">
        <el-icon class="fileDel">
            <Delete />
        </el-icon>
        <span>永久删除</span> 
    </el-button>

    <el-button type="success" style="height:38px ;margin:10px 0px 10px 10px" :disabled="selectFileIdList.length==0" @click="refreshDialog = true">
        <el-icon class="fileRefresh">
            <Refresh />
        </el-icon>
        <span>批量恢复</span> 
    </el-button>

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
          <div style="margin-left:20px; width:180px">
            <span class="userNameSpan" 
            style="cursor: pointer;" 
            @dblclick="spanDoubleClick">{{ scope.row.fileNewName }}</span>
          </div>
          <!-- {{scope.row.fileId}} -->
          
        </div> 
      </template>
    </el-table-column>
    <el-table-column label="操作" width="400" @mouseenter="showOptions(scope.row)"
          @mouseleave="hideOptions(scope.row)" >
      <template #default="scope" >
        <div>
          <div  
           v-show="scope.row.showOptions">
              <div style="margin:0px 0px 0px 160px;font-size: 24px">
                <el-icon title="恢复" @click="refresh(scope.row.fileId)"><Refresh /></el-icon>
                <el-icon title="永久删除" @click="openDelDialog(scope.row.fileId)"><DeleteFilled /></el-icon>
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

  <el-dialog  draggable  align-center  v-model="openDialog" 
    title="警告" width="500">
      <div>
        <span>确认要将这<span style="color:#1197fe;margin-left:5px">{{selectFileIdList.length>0?selectFileIdList.length:1}}</span>
        个文件<span style="color:#f56c6c;margin:0px 5px">永久删除</span>吗?</span>
      </div>
      <div>
        <span>-文件将<span style="color:#f56c6c;">无法恢复</span></span>
      </div>
      <template #footer>
        <div class="dialog-footer">
            <el-button @click="openDialog = false">取消</el-button>
            <el-button plain type="danger" @click="delForever()">
            确定</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog  draggable  align-center  v-model="refreshDialog" 
    title="提示" width="500">
      <div>
        <span>确认要将这<span style="color:#1197fe;margin:0px 5px"> {{ selectFileIdList.length }}</span>
        个文件从回收站恢复吗?</span>
      </div>
      <template #footer>
        <div class="dialog-footer">
            <el-button @click="refreshDialog = false">取消</el-button>
            <el-button  type="primary" @click="refresh()">
            确定</el-button>
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
.fileDel:hover{
    color: #ffffff
}
.fileRefresh:hover{
    color: #ffffff
}

</style>