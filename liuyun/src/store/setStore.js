//存储日程信息的pinia

import {defineStore} from "pinia"

export const setStore=defineStore(
    "setStore",
    {
        state:()=>{
            return{
                isCollapse:false,
                isRemember:false,           
            }
        },
        getters:{

        },
        actions:{

        },
        persist:true
    }
)