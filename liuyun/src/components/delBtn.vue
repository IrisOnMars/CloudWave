<script setup>
import { ref,reactive ,watch, onMounted} from "vue"
import { useRoute,useRouter } from 'vue-router'
import request from "/src/utils/request"
import { userStore } from '../store/userStore';
import { ElNotification } from 'element-plus'
import {computed} from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { moveStore } from "../store/moveStore";

const sysUser=userStore()
const router=useRouter()
const route=useRoute()

const props=defineProps(['selectLength','selectedFiles',])
const emit = defineEmits(['data-updated',])

const list=reactive({file:[]})
const openDialog=ref(false)

async function moveToBin(){
    
    openDialog.value = false
    list.file=props.selectedFiles
    console.log("删除目标")
    console.log(list.file)
    const {data}= await request.post("/file/moveToBin",list.file)
    const total=data.data.total
    const count=data.data.count
    if(data.code==200){
        
        ElNotification({
            title: '回收成功',
            type:'success',
            message: `检测到${total}个文件，实际移动${count}个至回收站`,
            position: 'bottom-left',
        })

        const folder={fileId:route.query.currentId||0}
        emit('data-updated',folder)
    }else{
        ElNotification({
            title: '出差错了',
            type:'warning',
            message: `检测到${total}个文件，实际移动${count}个至回收站`,
            position: 'bottom-left',
        })
    }
}

</script>

<template>
<div>
    <el-button type="danger" style="height:38px" :disabled="props.selectLength==0" @click="openDialog = true">
    <el-icon class="fileDel">
          <Delete />
    </el-icon>
    <span>批量删除</span> 
    </el-button>

    <el-dialog  draggable  align-center  v-model="openDialog" 
    title="确认删除" width="500">
      <div>
        <span>确认要将这<span style="color:#1197fe;margin:0px 5px"> {{ props.selectLength }}</span>个文件夹移入回收站吗?</span>
      </div>
      <div>
        <span>文件会在回收站保留7天。</span>
      </div>
      <template #footer>
        <div class="dialog-footer">
            <el-button @click="openDialog = false">取消</el-button>
            <el-button type="primary" @click="moveToBin">
            确定</el-button>
        </div>
      </template>
    </el-dialog>
</div>
</template>

<style scoped>
.el-button{
  margin-right: 10px;
}
.fileDel{
  margin: 0;
  font-size:16px; 
}
</style>