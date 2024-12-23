<template>
    <div class="admin-navigation">
        <h3 class="admin-title">Hestia 管理員</h3>
        <ul class="admin-nav">
            <li v-for="(item, index) in adminLinks" :key="index">
                <router-link :to="item.path" class="admin-nav-link">
                    {{ item.label }}
                </router-link>
            </li>
        </ul>
    </div>
</template>

<script setup>
import { ref } from 'vue'
import Login from './secure/Login.vue'

const adminLinks = ref([
    { label: '食材管理', path: '/ingredients-list' },
    { label: '商品管理', path: '/products-list' },
    { label: '訂單管理', path: '/orders-list' },
    { label: '管理者列表', path: '/admin-list' }
])
// const register = async () => {
//     try {
//         const response = await axios.post('/admin/register', {
//             account: this.account,
//             password: this.password,
//             name: this.name
//         });
//         console.log(response.data);  // 打印返回的信息（如："管理员注册成功"）
//     } catch (error) {
//         console.error('注册失败:', error.response.data);  // 打印错误信息（如："账号已存在"）
//     }
// };

// const login = async () => {
//     try {
//         const response = await axios.post('/admin/login', {
//             account: this.account,
//             password: this.password
//         });
//         console.log(response.data);  // 打印返回的信息（如："登录成功"）
//     } catch (error) {
//         console.error('登录失败:', error.response.data);  // 打印错误信息（如："账号或密码错误"）
//     }
// };


</script>

<style scoped>
/* 中心導航容器 */
.admin-navigation {
    background: linear-gradient(135deg, #f8f9fa, #e9ecef);
    border-radius: 12px;
    padding: 30px;
    /* 增加內邊距 */
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    width: 400px;
    /* 增加框的寬度 */
    margin: 40px auto;
    text-align: center;
}

/* 導航連結樣式 */
.admin-nav-link {
    text-decoration: none;
    color: #555;
    display: flex;
    /* 使用 flex 布局置中文字 */
    align-items: center;
    /* 垂直置中 */
    justify-content: center;
    /* 水平置中 */
    padding: 15px;
    /* 內邊距 */
    font-size: 22px;
    /* 字體變大 */
    font-weight: 500;
    border: 1px solid #ddd;
    background-color: #fff;
    border-radius: 8px;
    width: 100%;
    /* 讓按鈕寬度充滿父容器 */
    height: 60px;
    /* 設定選項高度 */
    box-sizing: border-box;
    transition: all 0.3s ease;
}

/* 每個列表項目 */
.admin-nav li {
    margin: 20px 0;
    /* 上下間距 */
}

/* 滑鼠懸停效果 */
.admin-nav-link:hover {
    background-color: #f8f9fa;
    /* 替換為淺灰色 */
    color: #007bff;
    /* 文字顏色改為深藍，與背景形成對比 */
    border-color: #f8f9fa;
    transform: translateX(5px);
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    /* 可減少陰影濃度 */
}




/* 標題樣式 */
.admin-title {
    color: #444;
    font-weight: 700;
    font-size: 26px;
    margin-bottom: 25px;
    margin-top: 25px;
    text-align: center;
}

/* 導航清單樣式 */
.admin-nav {
    list-style: none;
    padding: 0;
    margin: 0;
}



/* 每個列表項目動畫延遲 */
.admin-nav li:nth-child(1) {
    animation-delay: 0.2s;
}

.admin-nav li:nth-child(2) {
    animation-delay: 0.4s;
}

.admin-nav li:nth-child(3) {
    animation-delay: 0.6s;
}

.admin-nav li:nth-child(4) {
    animation-delay: 0.8s;
}




/* 當前選中連結效果 */
.admin-nav-link.router-link-exact-active {
    background-color: #28a745;
    color: #fff;
    border-color: #28a745;
    box-shadow: 0 4px 10px rgba(40, 167, 69, 0.2);
}

/* 動畫效果 */
@keyframes fadeIn {
    from {
        opacity: 0;
        transform: scale(0.9);
    }

    to {
        opacity: 1;
        transform: scale(1);
    }
}

@keyframes slideIn {
    from {
        opacity: 0;
        transform: translateX(-50px);
    }

    to {
        opacity: 1;
        transform: translateX(0);
    }
}
</style>