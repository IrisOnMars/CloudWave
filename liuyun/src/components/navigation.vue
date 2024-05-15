<script setup>
import {ref,reactive, watch, onMounted} from "vue"
import { useRoute,useRouter } from 'vue-router'
import { userStore } from '../store/userStore'
import request from "/src/utils/request"
const router=useRouter()
const route=useRoute()
const sysUser=userStore()
const props=defineProps({
    watchPath:{
        type:Boolean,
        default:true,
    },
    route:{
        type:Object
    }
})
const emit = defineEmits(['folder-updated','all-updated'])

const folderList=reactive([])
const currentFolder=reactive({
    userName:sysUser.userName,
    fileNewName:'',
    fileId:0
})

onMounted(
   ()=>{

     let filePath=''
    if(props.watchPath===true){
       filePath=props.route.query.path
    }
    
    console.log("filePath")
        console.log(filePath)
    // 使用split方法将字符串以'/'分割
    if(filePath!=null){
        let pathParts = filePath.split('/');

        // 遍历分割后的路径部分，并为每个部分创建一个新对象添加到fileList中
        pathParts.forEach(part => {
        // 如果part不是空字符串（即不是路径末尾的额外'/'），则创建一个新对象
        if (part) {
            folderList.push({ fileNewName: part  });
        }
        });
        console.log("folderList")
        console.log(folderList)
    }
} 
)
const openFolder=(file)=>{
    const {fileNewName,fileId,userName}=file
    const folder=reactive({
        userName:userName,
        fileNewName:fileNewName,
        fileId:fileId
    })
    folderList.push(folder)
    Object.assign(currentFolder, folder);
    setPath()
}
defineExpose({openFolder})

const setPath=()=>{
    if(!props.watchPath){
        //设置不监听路由
        emit("all-updated" ,currentFolder)
        return;
    }
    
    let pathArray=[]
    folderList.forEach(item=>{
        pathArray.push(item.fileNewName)
    })
    router.push({
        path:route.path,
        query:pathArray.length==0?"":{path:pathArray.join('/'),currentId:currentFolder.fileId}
    })
    
}

const setCurrentFolder=(index)=>{
    if(index==-1){
        currentFolder.fileNewName='全部文件'
        currentFolder.fileId=0
        folderList.splice(0, folderList.length)
        // console.log("currentFolder"+currentFolder.fileNewName)
        // console.log("folderList"+folderList)
    }else{
        currentFolder.fileNewName=folderList[index].fileNewName
        currentFolder.fileId=folderList[index].fileId
        folderList.splice(index+1,folderList.length)
        // console.log("currentFolder"+currentFolder)
        // console.log("folderList"+folderList)
    }

    setPath()
}

watch(
    ()=>route,
    (newVal,oldVal)=>{
        
        if(!props.watchPath){
            
            return;
        }
        if(newVal.path.indexOf("/all")===-1){ //如果不是all路径下的改变，不监听
            
            return;
        }
        const path=newVal.query.path;
        
        if(path==undefined){
            console.log("文件夹名称"+currentFolder.fileNewName)
            console.log("文件夹ID"+currentFolder.fileId)
            emit("all-updated" ,currentFolder)
        }else{
            // getNavigationFolder(path);
            let pathArray=path.split("/")
            //currentFolder.fileNewName=pathArray[pathArray.length-1]
            console.log("文件夹名称"+currentFolder.fileNewName)
            console.log("文件夹ID"+currentFolder.fileId)
            emit("folder-updated",currentFolder)
        }

    },
    {immediate:true ,deep:true}
)
const backParent=()=>{
    console.log("csssssssssssssssss")
     console.log(folderList[folderList.length-2])
     console.log(folderList)
     if(folderList.length-1<=0){
        currentFolder.fileNewName=''
        currentFolder.fileId=0
        folderList.splice(0,folderList.length)
     }else{
        currentFolder.fileNewName=folderList[folderList.length-2].fileNewName
        currentFolder.fileId=folderList[folderList.length-2].fileId
        folderList.splice(folderList.length-1,1)}
    setPath()
}

</script>

<template>
    <div class="top-navigation" >  
        <div v-if="folderList.length > 0" >
            <span class="back link" @click="backParent">返回上一级</span>
            <el-divider direction="vertical"></el-divider>
        </div>
        <span v-if="folderList.length == 0" class="all-file">全部文件</span>
        <span v-if="folderList.length > 0" class="link" 
            @click="setCurrentFolder(-1)">全部文件</span>
        <div v-for="(item,index) in folderList " >
            <el-icon><ArrowRightBold /></el-icon>
            <span class="link" 
            v-if="index < folderList.length-1" 
            @click="setCurrentFolder(index)"> 
            {{ item.fileNewName }}</span>
            <span v-if="index == folderList.length - 1" class="text">
            {{item.fileNewName}}
            </span>
        </div>
    </div>
 </template>
    

<style lang="scss" scoped>
.top-navigation{
    font-size: 13px;
    display: flex;
    align-items:center;
    line-height: 40px;
    .all-file{
        color: #5a5a5a;
    }
    .link{
        color: #06a7ff;
        cursor: pointer;
    }
    .text{
        color: #5a5a5a;
    }
    .el-icon{
        color: #06a7ff;
        padding: 0px 5px;
        font-size: 10px;
    }
}
</style>
