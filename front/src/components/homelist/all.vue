<script  setup>
import { Timer } from '@element-plus/icons-vue'
import { fileStore } from '../../store/fileStore'
import { userStore } from '../../store/userStore'
import myicon from '/src/components/myicon.vue'
import request from "../../utils/request"
import {onMounted, reactive, ref} from 'vue'
import { useRoute,useRouter } from 'vue-router'
import { ElMessage, ElMessageBox ,ElNotification} from 'element-plus'
import { iconStore } from '/src/store/iconStore'
import Btnlist from '../btnlist.vue'
const formLabelWidth="120px"
const icon=iconStore()
const router=useRouter()
const route=useRoute()
const sysUser=userStore()
// const percent=ref(0)

// function handleProgress(progressEvent, file, fileList) {//文件上传时的钩子
//       percent.value=Math.round(progressEvent.percent * 100) / 100
//       console.log(`当前进度：${progressEvent.percent}%`);
//     }


// function  progressStatus(){//文件进度状态
//   if(percent.value==100){
//     return "success"
//   }
//   return null
// }
function  handleSuccess(response, file, fileList) {
      console.log('文件上传成功');
    }
const handleEdit = (index, row) => {
  console.log(index, row)
}
const handleDelete = (index, row) => {
  console.log(index, row)
}
// const tableData=reactive([
//   {
//       fileName:"r",
//       updateTime:"2",
//       fileSize:"1144", 
//   },
// ])
const fileList=fileStore()
const folder=reactive({
  fileId:route.query.currentId||0,
  userName:sysUser.userName
})
onMounted(
  async()=>{

    
    if(route.query.currentId!=undefined){
      folder.fileId=route.query.currentId
    }
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
function share(){
  
 
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
  const {data}= await request.post("/file/getFiles",folder)
  if(data.code==200){

  fileList.receiveFiles(data.data.fileList)
  console.log('数据已更新'); 
  ElMessage.success('数据已更新'); 
  }
  else{
 
  }
}
async function enterFolder(folder){

  const folderMsg={
    fileId:folder.fileId,
    userName:folder.userName
  }
  const {data}= await request.post("/file/enterFolder",folderMsg)
    if(data.code==200){
      console.log(data)
    //   data.data.fileList.forEach(item => {
    // // 将ISO 8601格式的字符串转换为Date对象
    //   const createTime = new Date(item.createTime);    
    //   // 格式化日期时间为"YYYY-MM-DD HH:mm:ss"的形式
    //   const formattedCreateTime = createTime.toLocaleString()
    //   // 将格式化后的字符串赋值回createTime字段
    //   item.createTime = formattedCreateTime;
    //   })
      
    fileList.receiveFiles(data.data.fileList)
    //ElMessage.success('数据已更新'); 
    }
    else if(data.code==506){
      fileList.receiveFiles(data.data.fileList)
      ElMessage.error('空文件夹'); 
    }
}
const navigationRef=ref()

function clickFolder(row,column,event){
  audioPlayerVisible.value=false;
  imageVisible.value=false
  videoPlayerVisible.value=false;
  if(column.label=='文件名'){
    if(row.isFolder==1){
      navigationRef.value.openFolder(row)

  //     mypath.value=mypath.value+'/'+row.fileNewName
  //  // 修改路由参数
  //   router.push({path:'/homepage/home',query:{filePath:mypath.value}});
     //ElMessage.success('你进入了'+row.fileNewName); 
      enterFolder(row)
    }else if(row.fileSize<104857600){
      if(row.fileCategory=='audio'){
        audioPlayerVisible.value=true;
        currentAudioUrl.value=`https://condor-light-suddenly.ngrok-free.app/myfiles/${row.userName}/${row.fileName}`
      }else if(row.fileCategory=='image'){
        console.log("1")
        imageVisible.value=true
        imageSrcList.value=[`https://condor-light-suddenly.ngrok-free.app/myfiles/${row.userName}/${row.fileName}`]
        console.log(imageSrcList.value)
        
      }else if(row.fileCategory=='video'){
        videoPlayerVisible.value=true;
        currentVideoUrl.value=`https://condor-light-suddenly.ngrok-free.app/myfiles/${row.userName}/${row.fileName}`
      }else{
        ElMessage.error('不支持预览，请下载后查看')
      }
    }else{
      ElMessage.error('文件过大，请下载后查看')
    }
    
  }
}


function spanDoubleClick(event){
  event.preventDefault()
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
    fileNewName:'',
    fileId:0,
    filePid:0
  })
function openRenameDialog(row){
  
  newNameFile.fileNewName=row.fileNewName,
  newNameFile.filePid=row.filePid
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
      ElMessage.error(`此目录中已存在 ${newNameFile.fileNewName}`)
    }
}


async function download(row) {
   // 这应该是与后端配置的 token 相匹配的 token
  const response = await fetch(`https://condor-light-suddenly.ngrok-free.app/download/${row.fileId}`, {
    headers: {
      'Authorization': localStorage.getItem('token'), // 设置请求头中的 token
    },
    method: 'GET',
  });

  if (!response.ok) {
    ElMessage.error("下载失败！")
    throw new Error(`HTTP error! status: ${response.status}`);
  } else {
    const url = window.URL.createObjectURL(new Blob([await response.blob()]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', `${row.fileNewName}`); // 设置下载文件的名称
    document.body.appendChild(link);
    link.click();
    ElMessage.success("下载成功！")
  }
}
const shareFile=reactive({
  fileNewName:'',
  fileId:0,
  accessCode:'',
})
const setCode=ref(false)
const shareDialog=ref(false)
async function openShareDialog(row){
  Object.assign(shareFile,row)
  if(row.accessCode==null||row.accessCode==undefined){
    setCode.value=true
  }else{
    setCode.value=false
  }
  shareDialog.value=true
}


const shareFormRef=ref(null)
const validateCode =function(rule, value, callback) {
    let passwordReg=/^[a-zA-Z0-9]{5}$/  //请输入5位的数字或大小写字母       
  if (!passwordReg.test(shareFile.accessCode )){
    callback(new Error('请输入5位数字或大小写字母'))
  } else {
    callback()
  }
}

const rules = reactive({
  codeType: [{required:true,message:"请选择生成方式"}], 
  code: [{ validator: validateCode, trigger: 'blur' }], 
})
const shareForm=reactive({
  codeType:null,
})
let shareKey=1
const submitForm =function(formEl) {
  
  if (!formEl) {
    console.log("back")
    return
  }
  formEl.validate(async (valid) => {
    if (valid) {
        if(shareForm.codeType==1){
          shareFile.accessCode=null
        }
        console.log('submit!')
        const {data}=await request.post("/file/setAccessCode",shareFile)
        console.log(data)
        if(data.code==200){
            ElMessage.success("提取码生成成功！")
            //shareDialog.value=false
            shareKey=shareKey+1
            setCode.value=false
            shareFile.accessCode=data.data.shareFile.accessCode
            updateData(folder)
        }else{
            ElMessage.error("提取码生成失败！")
        }
    } else {
       
      return false
    }
  })
}

function goToShare(){
  window.open(`http://localhost:5173/#/share?fileId=${shareFile.fileId}`, '_blank');
}
const imageVisible=ref(false)
const audioPlayerVisible=ref(false)
const currentAudioUrl=ref(null)
const videoPlayerVisible=ref(false)
const currentVideoUrl=ref(null)
const imageSrcList=ref([''])
</script>


<template>
<div>
  <div class="btnBar">
    <Btnlist class="btnlist" @data-updated="updateData" :current-path="route"/>
    
    <DelBtn
    @data-updated="updateData" 
    :selectLength="selectFileIdList.length"
    :selectedFiles="selectFileIdList"
    ></DelBtn>
    <!-- 文件移动 -->
    <MoveBtn  
    @data-updated="updateData"  
    :selectLength="selectFileIdList.length"
    :selectedFiles="selectFileIdList"
    
    ></MoveBtn>

  </div>

  <!-- 导航栏 -->
  <Navigation ref="navigationRef" 
  @folder-updated="updateData" 
  @all-updated="updateData"
  :route="route"
  ></Navigation>

  <div v-if="audioPlayerVisible" class="audio-player"><!-- 音乐播放器 -->
    <audio :src="currentAudioUrl" controls autoplay style="width:100%;height:30px"></audio>
  </div>

  <div class="video-container" v-if="videoPlayerVisible"><!-- 视频播放器 -->
   <video :src="currentVideoUrl" id="myVideo" playsinline  autoplay controls></video>
  <div class="video-overlay" @click="videoPlayerVisible=false"></div>
</div>

  <div><!-- 图片预览 -->
   <el-image-viewer v-if="imageVisible" @close="imageVisible=!imageVisible"  :hide-on-click-modal="true" :url-list="imageSrcList" index="0"></el-image-viewer>
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
          <div style="margin-left:20px; width:200px;display:flex;align-items: center;">
            <el-text class="userNameSpan" truncated :title="scope.row.fileNewName" @dblclick="spanDoubleClick">
              {{ scope.row.fileNewName }}
            </el-text>
            <!-- <span class="userNameSpan" 
            style="cursor: pointer;" 
            @dblclick="spanDoubleClick">{{ scope.row.fileNewName }}</span> -->
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
                <el-icon title="下载" v-if="scope.row.isFolder==0?true:false" @click="download(scope.row)"><Download /></el-icon>
                <el-icon title="分享" v-if="scope.row.isFolder==0?true:false" @click="openShareDialog(scope.row)"><Share /></el-icon>
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
        <el-input v-model="newNameFile.fileNewName"
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

  <el-dialog :key="shareKey" class="dialog" v-model="shareDialog" title="文件分享" draggable top="35vh" width="500" >
    <el-form  ref="shareFormRef"
    :model="shareForm" 
    :rules="rules">
    <el-form-item label="文件名:" prop="" style="margin-top:15px" >
        <span>{{ shareFile.fileNewName }}</span>
    </el-form-item>
    <el-form-item label="分享链接:" prop="" style="color: #1197fe;margin-top:15px" >
        <span class="shareUrl" title="点击前往此页面" @click="goToShare">{{ `http://localhost:5173/#/share?fileId=${shareFile.fileId}` }}</span>
    </el-form-item>
    <el-form-item v-if="setCode" label="设置提取码"  prop="codeType" style="margin-top:15px" >
      <el-radio-group v-model="shareForm.codeType">
        <el-radio :label="0">自定义</el-radio> 
        <el-radio :label="1">系统生成</el-radio> 
      </el-radio-group>
    </el-form-item>
    <el-form-item   prop="code" v-if="shareForm.codeType==0&&setCode" >
        <el-input clearable 
        placeholder="请输入5位提取码"
        v-model="shareFile.accessCode"
        maxLength="5" style="width:130px">
        </el-input>
    </el-form-item>
    <el-form-item v-if="!setCode" label="提取码:" >
        <span style="color: #1197fe;">{{ shareFile.accessCode }}</span>
    </el-form-item>
    
    </el-form>
    <template #footer>
      <div class="dialog-footer"  >
          <el-button @click="shareDialog = false">取消</el-button>
          <el-button v-if="setCode" type="primary" @click="submitForm(shareFormRef)">
          生成提取码</el-button>
          <el-button v-else type="primary" @click="shareDialog = false">
          确定</el-button>
      </div>
    </template>
  </el-dialog>
  

</div>
</template>

<style scoped lang="scss">
.video-container {
 
  z-index: 997;
   /* Viewport height */
  overflow: hidden; /* Hide any content outside the container */
}

#myVideo {
  position: absolute;
  top: 0;
  top: 50%; /* 将组件的顶部移动到容器的中心 */
  left: 50%; /* 将组件的左边移动到容器的中心 */
  transform: translate(-50%, -50%);
  height: 100vh;
  z-index: 999;
  object-fit: cover; /* Ensure the video covers the entire container */
}

.video-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  z-index: 998;
  background-color: rgba(0, 0, 0, 0.5); /* Black overlay with opacity */
}
.audio-player{
  position: fixed;
  bottom: 0;
  
  width: 75vw;
  z-index: 9; /* 确保audio组件在内容之上 */
}
.btnBar{
  display:flex;
  .btnlist{
    margin-right:10px;
  }
}
.rowClass{
  padding: 0;
}
.fileNameColumn{
  display: flex ;
  height: 40px;
  align-items: center;
  &:hover{
    color: #1197fe;
  }
}
.userNameSpan{
  -moz-user-select : none;
  user-select:none;
  align-self: center;
  &:hover{
    color: #1197fe;
  }
}
.shareUrl{
  cursor:pointer;
  &:hover{
    text-decoration: underline;
  }
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
  margin-left:0
}
.UploadFilled:hover{
  color: #fff;
  
}
</style>