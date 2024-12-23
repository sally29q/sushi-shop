<template>
    <div class="ingredients-container">
        <h1 class="page-title">食材管理</h1>

        <!-- 搜尋欄 -->
        <div class="search-bar">
            <input class="search" v-model="searchTerm" type="text" placeholder="按Enter搜尋食材名稱"
                @keydown.enter="searchIngredients" />
            <input class="search" v-model="searchId" type="text" placeholder="按Enter根據ID搜索"
                @keydown.enter="searchIngredientById" />
        </div>

        <!-- 食材列表 -->
        <table class="ingredients-table">
            <thead>
                <tr>
                    <th class="col-2" style="text-align: center;">食材ID</th>
                    <th class="col-4" style="text-align: center;">食材名稱</th>
                    <th class="col-3" style="text-align: center;">總數量</th>
                    <th class="col-3" style="text-align: center;">操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="ingredient in ingredients" :key="ingredient.id">
                    <td>{{ ingredient.ingredientId }}</td>
                    <td>{{ ingredient.ingredientName }}</td>
                    <td>{{ ingredient.totalQuantity }}</td>
                    <td>
                        <button @click="editIngredient(ingredient)" class="edit-btn">編輯</button>
                        <button @click="deleteIngredient(ingredient.ingredientId)" class="delete-btn">刪除</button>
                    </td>
                </tr>
            </tbody>
        </table>

        <!-- 新增食材按钮 -->
        <button class="add-ingredient-btn" @click="addIngredient">新增食材</button>
        <button class="add-ingredient-btn" @click="goHome">返回首頁</button>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axiosapi from "@/plugins/axios.js"
import Swal from 'sweetalert2'
import 'sweetalert2/dist/sweetalert2.min.css'
import { useRouter } from 'vue-router'

const router = useRouter()
// 返回首頁
const goHome = () => {
    router.push('/')  // 跳轉到首頁
}

const ingredients = ref([])
const searchTerm = ref('')
const searchId = ref('')

// 列出所有食材
const getAllIngredients = async () => {
    try {
        const response = await axiosapi.get('/ingredients')
        ingredients.value = response.data
    } catch (error) {
        Swal.fire({
            icon: 'error',
            title: '錯誤',
            text: '無法取得食材數據，請稍後再試。',
        })
    }
}

// 根據食材名稱模糊搜尋
const searchIngredients = async () => {
    if (!searchTerm.value) {
        return getAllIngredients()
    }

    try {
        const response = await axiosapi.get(`/ingredients/search-by-name?ingredientName=${searchTerm.value}`)
        ingredients.value = response.data
    } catch (error) {
        Swal.fire({
            icon: 'error',
            title: '錯誤',
            text: '搜尋失敗，請稍後再試。',
        })
    }
}

// 根據食材 id 搜尋
const searchIngredientById = async () => {
    if (!searchId.value) {
        return getAllIngredients()
    }

    try {
        const response = await axiosapi.get(`/ingredients/${searchId.value}`)
        ingredients.value = response.data ? [response.data] : []
    } catch (error) {
        Swal.fire({
            icon: 'error',
            title: '錯誤',
            text: '搜尋失敗，請稍後再試。',
        })
    }
}

// 新增食材
const addIngredient = async () => {
    const { value: ingredientName } = await Swal.fire({
        title: '輸入食材名稱',
        input: 'text',
        inputLabel: '食材名稱',
        inputPlaceholder: '請輸入食材名稱',
        showCancelButton: true,
        cancelButtonText: '取消',
        confirmButtonText: '下一步',
        inputValidator: (value) => {
            if (!value) return '食材名稱不能為空'
        },
    })

    if (ingredientName) {
        const { value: totalQuantity } = await Swal.fire({
            title: '輸入食材總數量',
            input: 'number',
            inputLabel: '總數量',
            inputPlaceholder: '請輸入數量',
            showCancelButton: true,
            cancelButtonText: '取消',
            confirmButtonText: '新增',
            inputValidator: (value) => {
                if (!value || value <= 0) return '數量必須大於0'
            },
        })

        if (totalQuantity) {
            const newIngredient = { ingredientName, totalQuantity }
            try {
                await axiosapi.post('/ingredients', newIngredient)
                Swal.fire({
                    icon: 'success',
                    title: '成功',
                    text: '食材已成功新增',
                })
                getAllIngredients() // 新增成功後重新獲取食材列表
            } catch (error) {
                Swal.fire({
                    icon: 'error',
                    title: '錯誤',
                    text: '無法新增食材，請稍後再試。',
                })
            }
        }
    }
}

// 編輯食材
const editIngredient = async (ingredient) => {
    const { value: newQuantity } = await Swal.fire({
        title: '編輯食材數量',
        input: 'number',
        inputLabel: '總數量',
        inputValue: ingredient.totalQuantity,
        inputPlaceholder: '請輸入新的總數量',
        showCancelButton: true,
        cancelButtonText: '取消',
        confirmButtonText: '更新',
        inputValidator: (value) => {
            if (!value || value <= 0) return '數量必須大於0'
        },
    })

    if (newQuantity) {
        try {
            const updatedIngredient = { totalQuantity: Number(newQuantity) }
            console.log("updatedIngredient", updatedIngredient)
            await axiosapi.put(`/ingredients/${ingredient.ingredientId}`, updatedIngredient)
            Swal.fire({
                icon: 'success',
                title: '成功',
                text: '食材數量已更新',
            })
            getAllIngredients() // 更新成功後重新獲取食材列表
        } catch (error) {
            Swal.fire({
                icon: 'error',
                title: '錯誤',
                text: '無法更新食材數量，請稍後再試。',
            })
        }
    }
}

// 刪除食材
const deleteIngredient = async (ingredientId) => {
    const result = await Swal.fire({
        title: '確定刪除嗎？',
        text: '刪除後無法恢復',
        icon: 'warning',
        showCancelButton: true,
        cancelButtonText: '取消',
        confirmButtonText: '確定刪除',
        confirmButtonColor: '#d33',
    })

    if (result.isConfirmed) {
        try {
            await axiosapi.delete(`/ingredients/${ingredientId}`)
            Swal.fire({
                icon: 'success',
                title: '成功',
                text: '食材已成功刪除',
            })
            getAllIngredients() // 刪除成功後重新獲取食材列表
        } catch (error) {
            Swal.fire({
                icon: 'error',
                title: '錯誤',
                text: '無法刪除食材，請稍後再試。',
            })
        }
    }
}

onMounted(() => {
    getAllIngredients() // 頁面加載時獲取所有食材
})
</script>

<style scoped>
.search {
    margin-right: 20px;
    border: 1px solid gray;
    border-radius: 5px;
    text-align: center;
}

.ingredients-container {
    padding: 20px;
    width: 80%;
    margin: auto;
}

.page-title {
    text-align: center;
    font-size: 30px;
}

.search-bar {
    margin-bottom: 20px;
    text-align: center;
}

.search-bar input {
    padding: 8px;
    font-size: 16px;
    width: 200px;
}

.ingredients-table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 20px;
}

.ingredients-table th,
.ingredients-table td {
    padding: 10px;
    text-align: center;
    border: 1px solid #ddd;
}

.ingredients-table th {
    background-color: #f4f4f4;
}

.add-ingredient-btn {
    display: block;
    width: 200px;
    margin: 20px auto;
    padding: 10px;
    background-color: #5cb85c;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
}

.add-ingredient-btn:hover {
    background-color: #4cae4c;
}

.edit-btn,
.delete-btn {
    padding: 5px 10px;
    cursor: pointer;
    border-radius: 5px;
    border: none;
    margin-right: 10px;
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