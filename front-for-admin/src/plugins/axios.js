import axios from "axios";
import router from "../routers/router";

const instance = axios.create({
    baseURL: import.meta.env.VITE_API_URL,
});

// 添加請求攔截器，這樣每個請求都會自動帶上 JWT
// instance.interceptors.request.use(function (config) {
//     const token = localStorage.getItem("jwtToken");  // 假設JWT保存在localStorage中
//     if (token) {
//         config.headers["Authorization"] = `Bearer ${token}`;
//     }
//     return config;
// }, function (error) {
//     return Promise.reject(error);
// });

// 處理響應攔截器，根據需要重定向到403頁面
// instance.interceptors.response.use(
//     function (response) {
//         return response;
//     },
//     function (error) {
//         if (error.response && error.response.status === 403) {
//             router.push = "Login";
//         }
//         return Promise.reject(error);
//     }
// );

export default instance;
