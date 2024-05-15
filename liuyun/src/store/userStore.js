//存储用户信息的pinia

import {defineStore} from "pinia"

export const userStore=defineStore(
    "userStore",
    {
        state:()=>{
          
            return{
                userName:"",
                nickName:"",
                email:"",
                avatar:"/src/assets/user.png",
                token:"",  
                password:""  ,
                useSpace:0,
                totalSpace:0,           
            }
        },
       
        getters:{

        },
        actions:{
            receiveUser(userData) {
                this.userName = userData.localUser.userName; // 将后端传来的对象数据保存到仓库中的files状态中
                this.nickName = userData.localUser.nickName;
                this.email = userData.localUser.email;
                if(userData.localUser.avatar!=null){
                    this.avatar = userData.localUser.avatar;
                }
                this.token = userData.token;
                this.useSpace=userData.localUser.useSpace
                this.totalSpace=userData.localUser.totalSpace
            }
        },
        persist:true
        
    }
)