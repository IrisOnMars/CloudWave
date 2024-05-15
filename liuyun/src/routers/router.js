import {createRouter,createWebHashHistory }  from "vue-router"

import { userStore } from "../store/userStore"
import home from "../components/home.vue"
import user from "../components/user.vue"
import login from "../components/login.vue"
import homepage from "../components/homepage.vue"
import transmission from "../components/transmission.vue"
import register from "../components/register.vue"
import loginpage from "../components/loginpage.vue"
import forgot_pwd from "../components/forgot_pwd.vue"
import all from "../components/homelist/all.vue"
import image from "../components/homelist/image.vue"
import video from "../components/homelist/video.vue"
import audio from "../components/homelist/audio.vue"
import document from "../components/homelist/document.vue" 
import zip from "../components/homelist/zip.vue"
import bin from "../components/bin.vue"
import error from "../components/error.vue"
import share from "../components/share.vue"
import recent from "../components/recent.vue"
import others from "../components/homelist/others.vue"
import { setStore } from "../store/setStore.js"
 import pinia from "../pinia.js"



let router=createRouter({
    history:createWebHashHistory(),
    routes:[
        {
            path:"/homepage",
            component:homepage,
            meta: { requiresAuth: true },//需要认证的路由
            children:[
                {
                    path:"home",
                    component:home,
                    children:[
                        {
                            path:"all",
                            component:all
                        },
                        {
                            path:"image",
                            component:image
                        },
                        {
                            path:"video",
                            component:video
                        },
                        {
                            path:"audio",
                            component:audio
                        },
                        {
                            path:"document",
                            component:document
                        },
                        {
                            path:"zip",
                            component:zip
                        },
                        {
                            path:"others",
                            component:others
                        },
                    ]
                }, 
                {
                    path:"transmission",
                    component:transmission
                },      
                {
                    path:"recent",
                    component:recent
                },      
                {
                    path:"user",
                    component:user
                },
                {
                    path:"bin",
                    component:bin
                },
            ]
        },
        {
            path:"/",
            redirect:"/login"
        },
        {
            path:"/home",
            redirect:"/homepage/home/"
        },
        {
            path:"/homepage",
            redirect:"/homepage/home/"
        },
        {
            path:"/error",
            component:error
        },
        {
            path:"/share",
            component:share
        },  
        {
            path:"/",
            component:loginpage,
            children:[
                {
                    path:"login",
                    component:login
                },
                {
                    path:"register",
                    component:register
                },
                {
                    path:"forgot_pwd",
                    component:forgot_pwd
                } 
                
            ]
        }
        

    ]

})

router.beforeEach((to,from,next)=>{
    const sysUser=userStore(pinia)
    const sys=setStore(pinia)
    
    if (to.matched.some(record => record.meta.requiresAuth)) {
        // 检查Pinia存储中是否有用户名
        // 确保你已经从Pinia中导入了useStore函数
        if (sysUser.userName=="") {
          // 如果没有用户名，重定向到登录页
          next({
            path: '/login',
            query: { redirect: to.fullPath } // 可以将当前路由的路径作为查询参数保存，以便登录后重定向回来
          });
        } else {
            
          // 如果有用户名，允许访问
          next();
        }
      } else {
        // 不需要认证的路由，直接访问
        next();
      }
    

})

export default router