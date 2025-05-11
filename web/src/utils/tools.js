import router from "../router/index.js"
import {ElMessage} from "element-plus";
import http from "@/utils/http.js";

const tools = {

    isLogin() {
        return localStorage.getItem("currentUser") !== null;
    },
    getCurrentUser() {
        return JSON.parse(localStorage.getItem("currentUser"));
    },
    getToken() {
        return localStorage.getItem("token");
    },
    formatDateToYYYYMMDD(date) {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are 0-based
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    }
}
export default tools
