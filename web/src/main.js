import {createApp} from 'vue'
import {createPinia} from 'pinia'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
// element-plus图标
import * as ElementPlusIconsVue from "@element-plus/icons-vue"
import TuiPlus from "@wocwin/t-ui-plus"
import "@wocwin/t-ui-plus/lib/style.css"

import App from './App.vue'
import router from './router'
const app = createApp(App)
// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

// import "./styles/common.css";

app.use(createPinia())
app.use(router)
app.use(ElementPlus)
app.use(TuiPlus)
app.mount('#app')
