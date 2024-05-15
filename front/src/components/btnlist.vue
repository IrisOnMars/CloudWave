<script setup>
import { ref,reactive ,watch} from "vue"
import { useRoute,useRouter } from 'vue-router'
import request from "/src/utils/request"
import { userStore } from '../store/userStore';
import {computed} from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'


const sysUser=userStore()
const router=useRouter()
const route=useRoute()
const newFolderDialog=ref(false)

const props=defineProps(['currentPath',])
const emit = defineEmits(['data-updated',])
const formLabelWidth="120px"

//fileList.fileData=props.currentFileList
const folder = reactive({
  fileNewName: '新建文件夹',
  userName:sysUser.userName,
  fileId:route.query.currentId,
  
})

function goToUpload(){
  router.push({path:'/homepage/transmission',query:{autoClick: true,currentId:route.query.currentId}})
}

async function newFolder(){
    
    console.log("文件夹名称"+folder.userName)
    const {data}= await request.post("/file/newFolder",folder)
    if(data.code==200){
        ElMessage.success("新建成功")
        emit('data-updated',folder)
        newFolderDialog.value = false
    }else{
        ElMessage.error(`已经存在命为 ${folder.fileNewName} 的文件夹了`)
    }
}

function openNewFolderDialog(){
    newFolderDialog.value = true
    let path=props.currentPath.query.path||''
    let pathArray=path.split("/")
    
    folder.fileId=props.currentPath.query.currentId
    console.log("propsId"+folder.fileId)
    
}

// watch(
  
//     ()=>props.selectedFiles,
//     (newVal,oldVal)=>{
//       localSelect.value=newVal
//       console.log(localSelect.value)
//     },
//     {immediate:true ,deep:true}
// )

// const localSelect=ref()
// function openMoveFolderDialog(){
//     localSelect.value=props.selectedFiles
//     //console.log(test)
   
//     updateData(folder)
//     console.log("数据")
//     console.log(localSelect.value)
//     console.log(props.selectedFiles)
    
//     moveFolderDialog.value=true
      
    
// }

// function spanDoubleClick(event){
//   event.preventDefault()
// }
// let selectedRows=[]
// function handleSelect(selection, row) {
//       selectedRows = selection;
// }
// function handleSelectAll(selection){
  
//   selectedRows = selection;
// }

// function printSelected(){
//   console.log(selectedRows);
// }

// const navigationRef=ref()

// function clickFolder(row,column,event){
//   if(column.label=='文件名'){
//     if(row.isFolder==1){
//       navigationRef.value.openFolder(row)

//   //     mypath.value=mypath.value+'/'+row.fileNewName
//   //  // 修改路由参数
//   //   router.push({path:'/homepage/home',query:{filePath:mypath.value}});
//      //ElMessage.success('你进入了'+row.fileNewName); 
//       enterFolder(row)
//     }
    
//   }
// }

// async function enterFolder(folder){
  
//   const folderMsg={
//     fileId:folder.fileId
//   }
//   const {data}= await request.post("/file/enterFolder",folderMsg)
//     if(data.code==200){
//       console.log(data)
//       data.data.fileList.forEach(item => {
//     // 将ISO 8601格式的字符串转换为Date对象
//       const createTime = new Date(item.createTime);    
//       // 格式化日期时间为"YYYY-MM-DD HH:mm:ss"的形式
//       const formattedCreateTime = createTime.toLocaleString()
//       // 将格式化后的字符串赋值回createTime字段
//       item.createTime = formattedCreateTime;
//       })
      
//     localList.receiveFiles(data.data.fileList)
//     //ElMessage.success('数据已更新'); 
//     }
//     else if(data.code==506){
//       localList.receiveFiles(data.data.fileList)
//       ElMessage.error('空文件夹'); 
//     }
// }

// function formatFileSize(sizeInBytes) {
//   if (sizeInBytes === 0) return '0 B';

//   const units = ['B', 'KB', 'MB', 'GB', 'TB'];
//   let unitIndex = 0;

//   // Find the correct unit
//   while (sizeInBytes >= 1024 && unitIndex < units.length - 1) {
//       sizeInBytes /= 1024;
//       unitIndex++;
//   }

//   // Round the size to the nearest 2 decimal places
//   return sizeInBytes.toFixed(2)+units[unitIndex]
// }

// async function updateData(folder){
//   const {data}= await request.post("/file/getFiles",folder)
//   if(data.code==200){
//     data.data.fileList.forEach(item => {
//   // 将ISO 8601格式的字符串转换为Date对象
//     const createTime = new Date(item.createTime);    
//     // 格式化日期时间为"YYYY-MM-DD HH:mm:ss"的形式
//     const formattedCreateTime = createTime.toLocaleString()
//     // 将格式化后的字符串赋值回createTime字段
//     item.createTime = formattedCreateTime;
//     })
    
//     localList.receiveFiles(data.data.fileList)
//   ElMessage.success('数据已更新'); 
//   }
//   else{
 
//   }
// }

</script>

<template>

<div>
    <el-button type="primary" @click="goToUpload">
        <el-icon class="UploadFilled" >
            <UploadFilled />
        </el-icon>
        <span>上传文件</span>
    </el-button>

    <el-button type="success" @click="openNewFolderDialog(localSelect)" >
        <el-icon class="FolderAdd" >
            <FolderAdd />
        </el-icon>
        <span>新建文件夹</span> 
    </el-button>

    <el-dialog class="dialog" v-model="newFolderDialog" title="新建文件夹" draggable top="35vh" width="500" >
        <el-form :model="folder">
        <el-form-item label="文件夹名称" :label-width="formLabelWidth" style="margin-top:15px" >
            <el-input v-model="folder.fileNewName" 
            clearable
            style="width:260px" />
        </el-form-item>
        </el-form>
        <template #footer>
        <div class="dialog-footer"  >
            <el-button @click="newFolderDialog = false">取消</el-button>
            <el-button type="primary" @click="newFolder()">
            确认
            </el-button>
        </div>
        </template>
    </el-dialog>

    


</div>
</template>

<style lang="scss" scoped>

.el-button{
    height: 38px;
}
.el-form{
    justify-content: center;
    align-items:  center,
    
}
.fileNameColumn{
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