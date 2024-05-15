import axios from "axios"
import router from "../routers/router";

const instance=axios.create({
    baseURL:"https://condor-light-suddenly.ngrok-free.app/"
})
//请求拦截器
instance.interceptors.request.use(
    config=>{
        const token = localStorage.getItem('token'); // 假设token保存在localStorage中
        
          // 让每个请求携带自定义token
        config.headers.Authorization =  token;
        
        return config;
    },
    error=>{
        return Promise.reject(error)
    }
)
//响应拦截器
instance.interceptors.response.use(
    response=>{
        
        return response
    },
    error=>{
        if (error.response && error.response.status === 401) {
            // 清除token或做其他处理
            // store.commit('user/logout'); // 假设你使用Vuex管理用户状态
            // router.push('/login'); // 直接跳转到登录页面
            router.push('/error'); // 跳转到错误页面
          }
        return Promise.reject(error)
    }
)

export default instance