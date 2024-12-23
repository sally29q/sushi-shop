<template>
    <div class="login-container">
        <div class="login-card">
            <h3 class="login-title">Sushi Shop 管理員</h3>
            <form @submit.prevent="login" class="login-form">
                <div class="input-group">
                    <label for="account" style="margin-top: 15px;">帳號</label>
                    <input type="text" id="account" v-model="account" @focus="clearError" class="input-account"
                        placeholder="請輸入帳號" />
                </div>
                <div class="input-group">
                    <label for="password" style="margin-top: 15px;">密碼</label>
                    <div class="password-container">
                        <input :type="passwordVisible ? 'text' : 'password'" id="password" v-model="password"
                            @focus="clearError" class="input-field" placeholder="請輸入密碼" />
                        <input type="checkbox" id="show-password" v-model="passwordVisible"
                            class="show-password-checkbox" />
                        <label for="show-password" class="show-password-label" style="margin-top: 10px;">顯示密碼</label>
                    </div>
                </div>
                <div class="login-actions">
                    <button type="submit" class="login-btn">登入</button>
                </div>
                <p v-if="message" class="error-message">{{ message }}</p>
            </form>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import axiosapi from "@/plugins/axios.js";
import Swal from 'sweetalert2';
import { useRouter } from 'vue-router';
import useAccountStore from '@/store/account.js';

const accountStore = useAccountStore();
const router = useRouter();
const account = ref(null);
const password = ref(null);
const passwordVisible = ref(false);
const message = ref(null);

// const login = () => {
//     // 登入邏輯
// };

// 清空錯誤訊息
function clearError() {
    message.value = "";
}

// 登入功能
async function login() {
    Swal.fire({
        title: "登入中...",
        showConfirmButton: false,
        allowOutsideClick: false,
        didOpen: () => {
            Swal.showLoading();
        }
    });

    if (!account.value) account.value = null;
    if (!password.value) password.value = null;

    let request = {
        account: account.value,
        password: password.value,
    };

    // 清除登入狀態
    axiosapi.defaults.headers.authorization = "";
    accountStore.setAccount("");
    accountStore.setLogin(false);
    // localStorage.removeItem("token");

    try {
        const response = await axiosapi.post(`/admin/login`, request);

        if (response.data.success) {
            // 登入成功，儲存 Token
            // const token = response.data.token;
            // localStorage.setItem("token", token);

            // 更新 Axios Authorization Header
            // axiosapi.defaults.headers.authorization = `Bearer ${token}`;

            // 更新狀態管理
            accountStore.setAccount(response.data.account);
            accountStore.setLogin(true);

            // 跳轉到首頁
            Swal.fire({
                icon: "success",
                title: response.data.message,
            }).then(() => {
                router.push({ name: "Home-link" });
            });
        } else {
            // 顯示錯誤訊息
            message.value = response.data.message;
            Swal.fire({
                icon: "warning",
                title: response.data.message,
            });
        }
    } catch (error) {
        console.error("Error during login:", error);
        Swal.fire({
            icon: "error",
            title: "查詢失敗: " + error.message,
        });
    }
}

// 初始化時檢查是否已登入
// if (localStorage.getItem("token")) {
//     const token = localStorage.getItem("token");

//     // 設定 Axios Authorization Header
//     axiosapi.defaults.headers.authorization = `Bearer ${token}`;

//     // 更新狀態管理
//     accountStore.setAccount(localStorage.getItem("account") || "");
//     accountStore.setLogin(true);
//     router.push({ name: "Home-link" });
// }

</script>

<style scoped>
/* 背景設定 */
.login-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background: linear-gradient(135deg, #f1f1f1 0%, #d3d3d3 100%);
}

.login-card {
    background-color: #fff;
    padding: 40px;
    border-radius: 10px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
    width: 500px;
    text-align: center;
    animation: fadeIn 1s ease-in;
}

/* 標題 */
.login-title {
    font-size: 2rem;
    font-weight: 700;
    color: #333;
    margin-bottom: 20px;
}

/* 表單樣式 */
.login-form {
    width: 100%;
}

.input-group {
    margin-bottom: 20px;
}

.input-group label {
    display: block;
    font-size: 1rem;
    color: #333;
    margin-bottom: 8px;
}

/* 更新帳號輸入框樣式 */
.input-account {
    width: 55% !important;
    padding: 12px !important;
    font-size: 18px !important;
    border: 1px solid #ccc !important;
    border-radius: 8px !important;
    margin-left: 10px !important;
    outline: none;
    transition: all 0.3s ease;
}

.input-account:focus {
    border-color: #2575fc !important;
    box-shadow: 0 0 8px rgba(37, 117, 252, 0.6) !important;
}


.input-field {
    width: 60%;
    padding: 12px;
    font-size: 18px;
    border: 1px solid #ccc;
    border-radius: 8px;
    /* outline: none; */
    /* transition: all 0.3s ease; */
    margin-left: 10px;
}

.input-field:focus {
    border-color: #2575fc;
    box-shadow: 0 0 8px rgba(37, 117, 252, 0.6);
}

/* 按鈕 */
.login-btn {
    background-color: #e09e60;
    /* 溫暖的橘棕色，象徵火焰 */
    color: #fffaf2;
    /* 柔和的奶白色文字 */
    padding: 12px 20px;
    font-size: 1.1rem;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    transition: background-color 0.3s ease, color 0.3s ease;
    width: 100%;
}

.login-btn:hover {
    background-color: #c87f4e;
    /* 懸停時顏色加深，強調火焰感 */
    color: #ffffff;
    /* 懸停時文字變為純白色，更加明亮 */
}


/* 錯誤訊息 */
.error-message {
    color: #f44336;
    font-size: 0.9rem;
    margin-top: 15px;
}

/* 動畫 */
@keyframes fadeIn {
    0% {
        opacity: 0;
        transform: translateY(-50px);
    }

    100% {
        opacity: 1;
        transform: translateY(0);
    }
}

.password-container {
    display: flex;
    align-items: center;
    width: 90%;
}

.show-password-checkbox {
    margin-left: 10px;
}

.show-password-label {
    font-size: 14px;
    cursor: pointer;
}
</style>