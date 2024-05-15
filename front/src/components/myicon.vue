<script setup>
import { iconStore } from '../store/iconStore'
import { reactive, ref} from 'vue'
const icon=iconStore()
const props=defineProps(['fileType','width'])

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

//  function getFileCategory(mimeType) {
//    return mimeTypeToCategory[mimeType] || icon.default
//  }
function  getFileIcon(type) {
      if(type.startsWith('image/')) {
        return icon.image // 图片文件类型图标
      } else if (type.startsWith('video/')) {
        return icon.video // 视频文件类型图标
      } else if (type.startsWith('audio/')) {
        return icon.audio // 音频文件类型图标
      } else if (type.startsWith('text/')) {
        return icon.text // text 文件类型图标
      }else if(type=='folder'){
        return icon.folder
      }else {
        return mimeTypeToCategory[type] || icon.default      // 默认文件类型图标
      }
    } 
</script>

<template>
<div class="myicon">
 
    <img :src="getFileIcon(props.fileType)" :width="props.width" > 
</div>
</template>

<style scoped>
*{
 margin: 0;
}
.myicon{
  display: flex;
  justify-content: center;
  align-items: center;
}


</style>