<script setup>
import { ref,reactive ,watch, onMounted} from "vue"
import { useRoute,useRouter } from 'vue-router'
import request from "/src/utils/request"
import { userStore } from '../store/userStore';
import {computed} from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { moveStore } from "../store/moveStore";

const sysUser=userStore()
const router=useRouter()
const route=useRoute()
const dialogVisible = ref(false)
const moveFolderDialog=ref(false)
const props=defineProps(['selectLength','selectedFiles',])
const emit = defineEmits(['data-updated',])
const formLabelWidth="140px"

const localList=moveStore()
let navigationKey=0

//const test=reactive(props.test)

const folder = reactive({
        fileNewName: '全部文件',
        userName:sysUser.userName,
        fileId:0,
 })
//fileList.fileData=props.currentFileList
// const folder = reactive({
//   fileNewName: '新建文件夹',
//   userName:sysUser.userName,
//   fileId:route.query.currentId,
//   pFileNewName:''
// })


// watch(
  
//     ()=>props.selectedFiles,
//     (newVal,oldVal)=>{
//       localSelect.value=newVal
//       console.log(localSelect.value)
//     },
//     {immediate:true ,deep:true}
// )

function formattedCreateTime(data){
  const createTime = new Date(data.createTime);    
    // 格式化日期时间为"YYYY-MM-DD HH:mm:ss"的形式
  const formattedCreateTime = createTime.toLocaleString()
    // 将格式化后的字符串赋值回createTime字段
  return formattedCreateTime
}

const selectedFiles=reactive({files:[]})


async function openMoveFolderDialog(){
    folder.fileNewName= '根目录',
    folder.userName=sysUser.userName,             
    folder.fileId=0,
    
    navigationKey += 1
    moveFolderDialog.value=true
    
    updateFolder(folder)
   
}

function openConfirmDialog(){
  console.log("selectedRows")
  console.log(selectedRows)
  if(selectedRows.fileId==route.query.currentId||(route.query.currentId==undefined&&selectedRows.fileId==0)){
    ElMessage.warning("你选择的目录为当前目录，无需移动")
  }
  else{
    dialogVisible.value=true
  }
       
}

async function moveFolder(){

    selectedFiles.files.forEach(file => {//发送前修改pid
        file.filePid = selectedRows.fileId;
    console.log("selectedFiles")
    console.log(selectedFiles.files)
    });
    dialogVisible.value=false
    moveFolderDialog.value=false
    const {data}= await request.post("/file/moveFiles",selectedFiles.files)
    console.log("move")
    console.log(data)
    if(data.code==200){
        ElMessage.success('移动成功！')
        folder.fileId=route.query.currentId
    
        emit('data-updated',folder)
    }
    
}

function spanDoubleClick(event){
  event.preventDefault()
}
let selectedRows={fileNewName:"根目录",fileId:0}
function handleSelect(selection, row) {
      selectedRows = selection;
      
}
function handleSelectAll(selection){
  selectedRows = selection;
}
const myTable=ref()

function cellClick(row, event, column) {
      // if (myTable.currentRow === row) {
      //   myTable.value.setCurrentRow(null); // 取消高亮
      // } else {
      //   myTable.value.setCurrentRow(row); // 设置当前高亮行
      // }
      if(selectedRows==row){
        
        myTable.value.setCurrentRow()
        selectedRows={fileNewName:folder.fileNewName,fileId:row.filePid}
      } else{
        
        selectedRows=row

      }
       console.log("现在的选择是：")
      console.log(selectedRows)
}
  


function printSelected(){
  console.log(selectedRows);
}

const navigationRef=ref()

function dbClickFolder(row,column,event){
  selectedRows=row
  if(column.label=='文件名'){
    if(row.isFolder==1){
      navigationRef.value.openFolder(row)
      // folder.fileNewName=row.fileNewName
      // folder.fileId=row.fileId
  //     mypath.value=mypath.value+'/'+row.fileNewName
  //  // 修改路由参数
  //   router.push({path:'/homepage/home',query:{filePath:mypath.value}});
     //ElMessage.success('你进入了'+row.fileNewName); 
      updateFolder(row)
    }
    
  }
}

async function enterFolder(folder){
  
  const folderMsg={
    fileId:folder.fileId
  }
  const {data}= await request.post("/file/enterFolder",folderMsg)
    if(data.code==200){
      console.log(data)
      
      
    localList.receiveFiles(data.data.fileList)
    //ElMessage.success('数据已更新'); 
    }
    else if(data.code==506){
      localList.receiveFiles(data.data.fileList)
      ElMessage.error('空文件夹'); 
    }
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


async function updateFolder(file){
   folder.fileId=file.fileId
   folder.fileNewName=file.fileNewName
   selectedRows.fileId=file.fileId
   selectedRows.fileNewName=file.fileNewName
  const {data}= await request.post("/file/getFolders",folder)
  if(data.code==200){
  //   data.data.fileList.forEach(item => {
  // // 将ISO 8601格式的字符串转换为Date对象
  //   const createTime = new Date(item.createTime);    
  //   // 格式化日期时间为"YYYY-MM-DD HH:mm:ss"的形式
  //   const formattedCreateTime = createTime.toLocaleString()
  //   // 将格式化后的字符串赋值回createTime字段
  //   item.createTime = formattedCreateTime;
  //   })
   selectedFiles.files=props.selectedFiles
    const filter = selectedFiles.files.filter(file => file.isFolder === 1);
    const selectedFileIdsSet = new Set(filter.map(file => file.fileId));
     // 在 filter 函数中使用 Set 的 has 方法来检查 fileId
    const filteredFiles = data.data.fileList.filter(file => {
    return !selectedFileIdsSet.has(file.fileId);
    });
    localList.receiveFiles(filteredFiles)
    console.log("filteredFiles")
    console.log(filteredFiles)
    
    ElMessage.success('数据已更新'); 
  }
  else if(data.code==506){
      localList.receiveFiles(data.data.fileList)
      ElMessage.error('空文件夹'); 
    }
}

</script>

<template>
<div>
    <el-button type="warning" :disabled="props.selectLength==0" @click="openMoveFolderDialog">
        <el-icon class="FolderMove" >
            <Rank />
        </el-icon>
        <span>移动文件</span> 
    </el-button>

    <el-dialog class="moveDialog" v-model="moveFolderDialog" title="移动文件" draggable top="15vh" width="800" >
        <Navigation ref="navigationRef" 
        :key="navigationKey"
        @folder-updated="updateFolder" 
        @all-updated="updateFolder"
        :route="route"
        :watchPath="false"
        ></Navigation>

        <el-table ref="myTable"
        border
        :data="localList.fileData"
        highlight-current-row
        @cell-dblclick="dbClickFolder"
        @cell-click="cellClick"
        height="350px"
        style="width: 100%">
            
            <el-table-column label="文件名" width="350px" >
                <template #default="scope">
                <div class="fileNewNameColumn" >
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
            
            <el-table-column label="修改时间" width="200">
                <template #default="scope">
                    <span style="margin-left: 10px">{{ formattedCreateTime(scope.row) }}</span>
                </template>
            </el-table-column>
            <el-table-column label="大小">
                <template #default="scope">
                    <span v-if="!scope.row.isFolder" style="margin-left: 10px">
                        {{formatFileSize(scope.row.fileSize)  }}</span>
                </template>
            </el-table-column>

        </el-table>
        <template #footer>
        <div class="dialog-footer"  >
            <el-button @click="moveFolderDialog = false">取消</el-button>
            <el-button type="primary" @click="openConfirmDialog">
            选择
            </el-button>
        </div>
        </template>
    </el-dialog>

    <el-dialog  draggable  align-center  v-model="dialogVisible" 
    title="确认移动" width="500">
      <div>
        <span>确认要移动到<span style="color:#1197fe;margin:0px 5px"> {{ selectedRows.fileNewName }}</span>文件夹?</span>
      </div>
      <template #footer>
        <div class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="moveFolder">
            确定</el-button>
        </div>
      </template>
    </el-dialog>

    
</div>
</template>

<style lang="scss" scoped>
.el-button{
    height: 38px;
    margin-right: 10px;
}
.el-form{
    justify-content: center;
    align-items:  center,
 
}

.fileNewNameColumn{
  display: flex ;
  align-items: center;
  margin-left: 10px;
  &:hover{
    color: #1197fe;
  }
}
.dialog{
    display: flex;
    justify-content: center;
    align-items:  center,
   
}
.el-icon{
    font-size:16px;
}
</style>