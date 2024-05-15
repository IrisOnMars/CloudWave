import { createApp } from 'vue'
//import './style.css'
import 'element-plus/dist/index.css'
import ElementPlus from 'element-plus'
import App from './App.vue'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import router from './routers/router'
import pinia from './pinia.js'
import btnlist from '/src/components/btnlist.vue';
import navigation from './components/navigation.vue'
import myicon from './components/myicon.vue'
import moveBtn from './components/moveBtn.vue'
import delBtn from './components/delBtn.vue'
import mytable from './components/mytable.vue'

const app=createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
  }
app.component("Mytable",mytable)
app.component("MoveBtn",moveBtn)
app.component("Navigation",navigation) 
app.component("Btnlist",btnlist)
app.component("Myicon",myicon)
app.component("DelBtn",delBtn)
app.use(ElementPlus)
app.use(router)
app.use(pinia)
app.mount('#app')
