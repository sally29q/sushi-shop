<template>
    <div class="container">
        <h1 class="centered-title">管理員列表</h1>
        <table class="admin-table">
            <thead>
                <tr>
                    <th class="col-2" style="text-align: center;">ID</th>
                    <th class="col-4" style="text-align: center;">帳號</th>
                    <th class="col-3" style="text-align: center;">使用者名稱</th>
                    <th class="col-3" style="text-align: center;">操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="admin in admins" :key="admin.adminId">
                    <td style="text-align: center;">{{ admin.adminId }}</td>
                    <td style="text-align: center;">{{ admin.account }}</td>
                    <td style="text-align: center;">{{ admin.name }}</td>
                    <td>
                        <button @click="editAdmin(admin)" class="edit-btn">編輯</button>
                        <button @click="deleteAdmin(admin.adminId)" class="delete-btn">刪除</button>
                    </td>
                </tr>
            </tbody>
        </table>
        <button class="add-admin-button" @click="addAdmin">註冊管理員</button>
        <br>
        <button class="add-admin-button" @click="goHome">返回首頁</button>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axiosapi from "@/plugins/axios.js";
import Swal from 'sweetalert2'
import 'sweetalert2/dist/sweetalert2.min.css'
import { useRouter } from 'vue-router'

const router = useRouter()
// 返回首頁
const goHome = () => {
    router.push('/')  // 跳轉到首頁
}

const admins = ref([])

// 取得管理員資料
const getAdmins = async () => {
    try {
        const response = await axiosapi.get(`/admin/all`)
        admins.value = response.data
    } catch (error) {
        Swal.fire({
            icon: 'error',
            title: '錯誤',
            text: '無法取得管理員資料，請稍後再試',
            confirmButtonColor: '#d33',
            confirmButtonText: '確定'
        })
    }
}

// 註冊新管理員
const addAdmin = async () => {
    const { value: account } = await Swal.fire({
        title: '輸入帳號',
        input: 'text',
        inputLabel: '帳號',
        inputPlaceholder: '請輸入帳號',
        showCancelButton: true,
        cancelButtonText: '取消',
        confirmButtonText: '下一步',
        confirmButtonColor: '#3085d6',
        inputValidator: (value) => {
            if (!value) {
                return '請輸入帳號'
            }
        }
    })

    if (account) {
        const { value: password } = await Swal.fire({
            title: '輸入密碼',
            input: 'password',
            inputLabel: '密碼',
            inputPlaceholder: '請輸入密碼',
            showCancelButton: true,
            cancelButtonText: '取消',
            confirmButtonText: '註冊',
            confirmButtonColor: '#28a745',
            inputValidator: (value) => {
                if (!value) {
                    return '請輸入密碼'
                }
            }
        })

        if (password) {
            const adminData = { account, password }
            try {
                const response = await axiosapi.post(`/admin/register`, adminData)
                const newAdmin = response.data
                Swal.fire({
                    icon: 'success',
                    title: '成功',
                    text: `成功註冊管理員`,
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '確定'
                })
                getAdmins() // 註冊成功後重新獲取管理員列表
            } catch (error) {
                Swal.fire({
                    icon: 'error',
                    title: '錯誤',
                    text: '無法註冊管理員，請再試一次',
                    confirmButtonColor: '#d33',
                    confirmButtonText: '確定'
                })
            }
        }
    }
}
// 編輯管理員資料
const editAdmin = async (admin) => {
    const { value: newName } = await Swal.fire({
        title: '編輯管理員',
        input: 'text',
        inputLabel: '使用者名稱',
        inputValue: admin.name,
        inputPlaceholder: '請輸入新名稱',
        showCancelButton: true,
        cancelButtonText: '取消',
        confirmButtonText: '更新',
        confirmButtonColor: '#3085d6',
        inputValidator: (value) => {
            if (!value) {
                return '名稱不可為空'
            }
        }
    })

    if (newName) {
        try {
            const updatedAdmin = { ...admin, name: newName }
            const response = await axiosapi.put(`/admin/edit/${admin.adminId}`, updatedAdmin)
            Swal.fire({
                icon: 'success',
                title: '成功',
                text: '管理員資料已更新',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '確定'
            })
            getAdmins() // 更新資料後重新獲取管理員列表
        } catch (error) {
            Swal.fire({
                icon: 'error',
                title: '錯誤',
                text: '無法更新管理員資料，請再試一次',
                confirmButtonColor: '#d33',
                confirmButtonText: '確定'
            })
        }
    }
}

// 刪除管理員
const deleteAdmin = async (adminId) => {
    const result = await Swal.fire({
        title: '確定刪除嗎？',
        text: '刪除後將無法恢復',
        icon: 'warning',
        showCancelButton: true,
        cancelButtonText: '取消',
        confirmButtonText: '確定刪除',
        confirmButtonColor: '#d33'
    })

    if (result.isConfirmed) {
        try {
            await axiosapi.delete(`/admin/delete/${adminId}`)
            Swal.fire({
                icon: 'success',
                title: '成功',
                text: '管理員已刪除',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '確定'
            })
            getAdmins() // 刪除成功後重新獲取管理員列表
        } catch (error) {
            Swal.fire({
                icon: 'error',
                title: '錯誤',
                text: '無法刪除管理員，請再試一次',
                confirmButtonColor: '#d33',
                confirmButtonText: '確定'
            })
        }
    }
}

onMounted(() => {
    getAdmins()
})
</script>

<style scoped>
.container {
    max-width: 800px;
    margin: 0 auto;
    padding: 20px;
}

.centered-title {
    color: #444;
    font-weight: 700;
    font-size: 26px;
    margin-bottom: 25px;
    margin-top: 25px;
    text-align: center;
}

.admin-table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 20px;
    background-color: #fff;
    border: 1px solid #ccc;
}

.admin-table th,
.admin-table td {
    text-align: left;
    padding: 10px;
    border: 1px solid #ddd;
}

.admin-table th {
    background-color: #f4f4f4;
}

.admin-table tr:nth-child(even) {
    background-color: #f9f9f9;
}

.add-admin-button {
    display: block;
    width: 200px;
    margin: 0 auto;
    padding: 10px 20px;
    background-color: #e09e60;
    /* 溫暖的橘棕色，象徵火焰 */
    color: #fffaf2;
    /* 柔和的奶白色文字 */
    border: none;
    border-radius: 5px;
    font-size: 16px;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

.add-admin-button:hover {
    background-color: #c87f4e;
    /* 懸停時變為更深的橘棕色，強化火焰感 */
}

/* 原样保留现有样式，新增编辑和删除按钮的样式 */
.edit-btn,
.delete-btn {
    padding: 5px 10px;
    margin-left: 15px;
    margin-right: 15px;
    border-radius: 5px;
    border: none;
    cursor: pointer;
}

.edit-btn {
    background-color: #ffc107;
    color: white;
}

.edit-btn:hover {
    background-color: #e0a800;
}

.delete-btn {
    background-color: #dc3545;
    color: white;
}

.delete-btn:hover {
    background-color: #c82333;
}
</style>
