
import {defineStore} from "pinia"

export const iconStore=defineStore(
    "iconStore",
    {
        state:()=>{
            return{
                image:'/src/assets/icon/image.svg',
                video:'/src/assets/icon/video.svg',
                audio:'/src/assets/icon/audio.svg',
                zip:'/src/assets/icon/zip.svg',
                text:'/src/assets/icon/text.svg',
                pdf:'/src/assets/icon/pdf.svg',
                ppt:'/src/assets/icon/ppt.svg',
                word:'/src/assets/icon/word.svg',
                excel:'/src/assets/icon/excel.svg',
                folder:'/src/assets/icon/folder.svg',
                default:'/src/assets/icon/default.svg'
               
            }
        },
        getters:{

        },
        actions:{
            
           
        },
        persist:true
        
    }
)