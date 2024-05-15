
import {defineStore} from "pinia"

export const uploadStore=defineStore(
    "uploadStore",
    {
        state:()=>{
            return{
                uploadList:[
    
                ]
               
            }
        },
        getters:{

        },
        actions:{
            addFile(file) {
                // 添加文件到列表
                this.uploadList.push({
                    uid:file.uid,
                    name: file.name,
                    size: file.size,
                    type: file.type,
                    category:'',
                    url:'',
                    percentage: 0, // 初始进度为 0
                    status:null,
                    
                  // 其他您需要的文件属性
                });
                console.log("yes")
              },
              removeFile(uid) {
                // 从列表中删除具有指定 uid 的文件
                const removedList = this.uploadList.filter(file => file.uid !== uid);
                
                const ifChange=this.uploadList.length !== removedList.length
                
                // 如果成功删除，更新 uploadList
                if (ifChange) {
                    
                    this.uploadList = removedList;
                    return ifChange;
                }
                    return ifChange;
                

              },
        },
        persist:true,
        
    }
)