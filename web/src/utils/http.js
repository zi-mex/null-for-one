import axios from "axios";
import router from "../router";
import {ElMessage} from "element-plus";

// 设置 Axios 的默认基础 URL
axios.defaults.baseURL = import.meta.env.VITE_APP_API_URL;

// 创建 Axios 实例
const http = axios.create({
    timeout: 5000, headers: {
        'Content-Type': 'application/json'
    }
});

// 请求拦截器
http.interceptors.request.use(config => {
    if (localStorage.getItem("token")) {
        config.headers["token"] = localStorage.getItem("token");
    }
    return config;
})
;

// 响应拦截器
http.interceptors.response.use(response => {
    return response.data;
}, error => {
    switch (error.response.status) {
        case 401:
            localStorage.removeItem("token");
            ElMessage({message: "请先登录", type: "error"});
            router.push("/login");
            break;
        case 409:
            ElMessage({message: error.response.data.data, type: "error"});
            break;
        case 404:
            ElMessage({message: "接口未找到", type: "error"});
            break;
        case 500:
            ElMessage({message: "服务异常", type: "error"});
            break;
        default:
            return Promise.reject(error);
    }
});
// 打印环境变量
console.log("环境:", import.meta.env.NODE_ENV);
console.log("服务器:", import.meta.env.VUE_APP_SERVER);
console.log("所有环境变量:", import.meta.env);
export default http;

