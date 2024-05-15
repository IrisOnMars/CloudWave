
import {defineStore} from "pinia"

export const fileStore=defineStore(
    "fileStore",
    {
        state:()=>{
            return{
                fileData:[
    
                ]
               
            }
        },
        getters:{

        },
        actions:{
            
            receiveFiles(files) {
                this.fileData = files; // 将后端传来的对象数据保存到仓库中的files状态中
              }
        },
        
        
    }
)