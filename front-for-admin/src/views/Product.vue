<template>
    <div class="products-container">
        <h1 class="page-title">商品管理</h1>

        <!-- 搜索栏 -->
        <div class="search-bar">
            <input class="search" v-model="searchTerm" type="text" placeholder="按Enter搜尋商品名稱"
                @keydown.enter="searchProductsByName" />
            <!-- <input class="search" v-model="searchCategory" type="text" placeholder="按Enter搜尋商品類別"
                @keydown.enter="searchProductsByCategory" /> -->
            <select v-model="searchCategory" @change="searchProductsByCategory">
                <option value="">請選擇類別</option>
                <option value="壽司">壽司</option>
                <option value="軍艦">軍艦</option>
                <option value="壽司卷">壽司卷</option>
                <option value="炸物">炸物</option>
                <option value="套餐">套餐</option>
            </select>
        </div>

        <!-- 商品列表 -->
        <table class="products-table">
            <thead>
                <tr>
                    <th class="col-1" style="text-align: center;">商品ID</th>
                    <th class="col-2" style="text-align: center;">商品圖片</th>
                    <th class="col-4" style="text-align: center;">商品名稱</th>
                    <th class="col-2" style="text-align: center;">類別</th>
                    <th class="col-1" style="text-align: center;">價格</th>
                    <th class="col-3" style="text-align: center;">操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="product in products" :key="product.productId">
                    <td>{{ product.productId }}</td>
                    <td>
                        <img v-if="product.photo" :src="'data:image/jpeg;base64,' + product.photo" alt="Product Image"
                            class="product-image" style="width: 50px; height: 50px;" />
                        <span v-else>無圖片</span>
                    </td>
                    <td>{{ product.productName }}</td>
                    <td>{{ product.category }}</td>
                    <td>${{ product.price }}</td>
                    <td>
                        <button @click="editProduct(product)" class="edit-btn">編輯</button>
                        <button @click="deleteProduct(product.productId)" class="delete-btn">刪除</button>
                    </td>
                </tr>
            </tbody>
        </table>

        <!-- 新增商品按钮 -->
        <button class="add-product-btn" @click="addProduct">新增商品</button>
        <button class="add-product-btn" @click="goHome">返回首頁</button>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axiosapi from "@/plugins/axios.js"
import Swal from 'sweetalert2'
import { useRouter } from 'vue-router'

const router = useRouter()
// 返回首頁
const goHome = () => {
    router.push('/')  // 跳轉到首頁
}

const products = ref([])
const searchTerm = ref('')
const searchCategory = ref('')

// 獲取所有商品
const getAllProducts = async () => {
    try {
        const response = await axiosapi.get('/products')
        products.value = response.data
    } catch (error) {
        Swal.fire({
            icon: 'error',
            title: '錯誤',
            text: '無法取得商品數據，請稍後再試。',
        })
    }
}

// 根據商品名稱模糊搜尋
const searchProductsByName = async () => {
    if (!searchTerm.value) {
        return getAllProducts()
    }
    try {
        const response = await axiosapi.get(`/products/search-by-name?productName=${searchTerm.value}`)
        products.value = response.data
    } catch (error) {
        Swal.fire({
            icon: 'error',
            title: '錯誤',
            text: '搜尋失敗，請稍後再試。',
        })
    }
}

// 根據商品類別搜尋
const searchProductsByCategory = async () => {
    if (!searchCategory.value) {
        return getAllProducts()
    }
    try {
        const response = await axiosapi.get(`/products/search-by-category?category=${searchCategory.value}`)
        products.value = response.data
    } catch (error) {
        Swal.fire({
            icon: 'error',
            title: '錯誤',
            text: '搜尋失敗，請稍後再試。',
        })
    }
}

// 編輯商品
const editProduct = async (product) => {
    try {
        // 通过商品ID获取商品信息和食材
        const { data: productData } = await axiosapi.get(`/products/${product.productId}`)

        // 步驟 1：修改商品基本資訊
        const { value: basicInfo } = await Swal.fire({
            title: '編輯商品資訊',
            html: `
        <form id="basicInfoForm">
        <div style="margin-bottom: 10px;">
            <label for="productName">商品名稱:</label>
            <input id="productName" type="text" class="swal2-input" value="${product.productName}" placeholder="請輸入商品名稱" />
        </div>
        <div style="margin-bottom: 10px;">
            <label for="category">商品類別:</label>
            <input id="category" type="text" class="swal2-input" value="${product.category}" placeholder="請輸入商品類別" />
        </div>
        <div style="margin-bottom: 10px;">
            <label for="price">商品價格:</label>
            <input id="price" type="number" class="swal2-input" value="${product.price}" placeholder="請輸入商品價格" />
        </div>
        <div style="margin-bottom: 10px;">
            <label for="photo">上傳圖片:</label>
            <input id="photo" type="file" class="swal2-input" />
        </div>
        </form>`,
            showCancelButton: true,
            cancelButtonText: '取消',
            confirmButtonText: '下一步',
            preConfirm: () => {
                const updatedProductName = document.getElementById('productName').value
                const updatedPrice = document.getElementById('price').value
                const updatedCategory = document.getElementById('category').value
                const updatedPhoto = document.getElementById('photo').files[0]

                if (!updatedProductName || !updatedPrice || !updatedCategory) {
                    Swal.showValidationMessage('所有字段都必須填寫')
                    return false
                }

                // 處理圖片上傳
                return new Promise((resolve) => {
                    let photoData = null
                    if (updatedPhoto) {
                        const reader = new FileReader()
                        reader.onload = () => {
                            photoData = reader.result.split(',')[1] // 獲取 base64 數據
                            resolve({
                                productId: product.productId,
                                productName: updatedProductName,
                                price: updatedPrice,
                                category: updatedCategory,
                                photo: photoData,
                            })
                        }
                        reader.readAsDataURL(updatedPhoto)
                    } else {
                        resolve({
                            productId: product.productId,
                            productName: updatedProductName,
                            price: updatedPrice,
                            category: updatedCategory,
                            photo: product.photo,
                        })
                    }
                })
            },
        })

        // 如果使用者填寫了商品基本資訊，進入下一步選擇食材
        if (basicInfo) {
            // 步驟 2：選擇食材
            const { value: ingredientDetails } = await Swal.fire({
                title: '選擇食材',
                html: `
            <div id="ingredientsList">
            ${Object.entries(productData.ingredientDetails).map(([ingredientName, quantity]) => `
            <div>
                <label for="ingredient_${ingredientName}">${ingredientName}:</label>
                <input id="ingredient_${ingredientName}" type="number" class="swal2-input" value="${quantity}" placeholder="数量" />
            </div>
            `).join('')}
            </div>
        `,
                showCancelButton: true,
                cancelButtonText: '取消',
                confirmButtonText: '完成',
                preConfirm: () => {
                    let selectedIngredients = {}
                    Object.entries(productData.ingredientDetails).forEach(([ingredientName, quantity]) => {
                        const ingredientQuantity = document.getElementById(`ingredient_${ingredientName}`).value
                        if (ingredientQuantity) {
                            selectedIngredients[ingredientName] = Number(ingredientQuantity)
                        }
                    })
                    return selectedIngredients
                },
            })

            // 如果使用者選擇了食材，提交商品和食材資料
            if (ingredientDetails) {
                const updatedProduct = {
                    ...basicInfo,
                    ingredientDetails: ingredientDetails,
                }

                try {
                    await axiosapi.put(`/products/${updatedProduct.productId}`, updatedProduct)
                    Swal.fire({
                        icon: 'success',
                        title: '成功',
                        text: '商品已更新',
                    })
                    getAllProducts() // 更新成功後重新獲取商品列表
                } catch (error) {
                    Swal.fire({
                        icon: 'error',
                        title: '錯誤',
                        text: '無法更新商品，請稍後再試。',
                    })
                }
            }
        }
    } catch (error) {
        Swal.fire({
            icon: 'error',
            title: '錯誤',
            text: '無法加載資料，請稍後再試。',
        })
    }
}


// 刪除商品
const deleteProduct = async (productId) => {
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
            await axiosapi.delete(`/products/${productId}`)
            Swal.fire({
                icon: 'success',
                title: '成功',
                text: '商品已成功刪除',
            })
            getAllProducts() // 刪除成功後重新獲取商品列表
        } catch (error) {
            Swal.fire({
                icon: 'error',
                title: '錯誤',
                text: '無法刪除商品，請稍後再試。',
            })
        }
    }
}

// 新增商品
// const addProduct = async () => {
//     const { value: productName } = await Swal.fire({
//         title: '输入商品名称',
//         input: 'text',
//         inputLabel: '商品名称',
//         inputPlaceholder: '请输入商品名称',
//         showCancelButton: true,
//         cancelButtonText: '取消',
//         confirmButtonText: '下一步',
//         inputValidator: (value) => {
//             if (!value) return '商品名称不能为空'
//         },
//     })

//     if (productName) {
//         const { value: category } = await Swal.fire({
//             title: '输入商品类别',
//             input: 'text',
//             inputLabel: '商品类别',
//             inputPlaceholder: '请输入商品类别',
//             showCancelButton: true,
//             cancelButtonText: '取消',
//             confirmButtonText: '下一步',
//         })

//         if (category) {
//             const { value: price } = await Swal.fire({
//                 title: '输入商品价格',
//                 input: 'number',
//                 inputLabel: '价格',
//                 inputPlaceholder: '请输入商品价格',
//                 showCancelButton: true,
//                 cancelButtonText: '取消',
//                 confirmButtonText: '下一步',
//             })

//             if (price) {
//                 const newProduct = { productName, price, category }
//                 try {
//                     await axios.post('/products', newProduct)
//                     Swal.fire({
//                         icon: 'success',
//                         title: '成功',
//                         text: '商品已成功新增',
//                     })
//                     getAllProducts() // 新增成功后重新获取商品列表
//                 } catch (error) {
//                     Swal.fire({
//                         icon: 'error',
//                         title: '错误',
//                         text: '无法新增商品，请稍后再试。',
//                     })
//                 }
//             }
//         }
//     }
// }
// 新增商品
const addProduct = async () => {
    try {
        // 獲取所有食材
        const { data: ingredients } = await axiosapi.get('/ingredients')

        // 步驟 1：輸入商品基本資訊
        const { value: productInfo } = await Swal.fire({
            title: '輸入商品資訊',
            html: `
                <form id="productInfoForm">
                    <div style="margin-bottom: 10px;">
                        <label for="productName">商品名稱:</label>
                        <input id="productName" type="text" class="swal2-input" placeholder="請輸入商品名稱" />
                    </div>
                    <div style="margin-bottom: 10px;">
                        <label for="category">商品類別:</label>
                        <input id="category" type="text" class="swal2-input" placeholder="請輸入商品類別" />
                    </div>
                    <div style="margin-bottom: 10px;">
                        <label for="price">商品價格:</label>
                        <input id="price" type="number" class="swal2-input" placeholder="請輸入商品價格" />
                    </div>
                    <div style="margin-bottom: 10px;">
                        <label for="photo">上傳圖片:</label>
                        <input id="photo" type="file" class="swal2-input" />
                    </div>
                </form>
            `,
            showCancelButton: true,
            cancelButtonText: '取消',
            confirmButtonText: '下一步',
            preConfirm: () => {
                const productName = document.getElementById('productName').value
                const category = document.getElementById('category').value
                const price = document.getElementById('price').value
                const photo = document.getElementById('photo').files[0]

                if (!productName || !category || !price) {
                    Swal.showValidationMessage('所有欄位都必須填寫')
                    return false
                }

                // 如果沒有上傳圖片，設置為 null
                let photoData = null
                if (photo) {
                    const reader = new FileReader()
                    reader.onload = () => {
                        photoData = reader.result.split(',')[1] // 獲取 base64 數據
                    }
                    reader.readAsDataURL(photo)
                }

                return {
                    productName,
                    category,
                    price,
                    photo: photoData,
                }
            },
        })

        if (productInfo) {
            const { productName, category, price, photo } = productInfo

            // 步驟 2：選擇食材
            const { value: ingredientDetails } = await Swal.fire({
                title: '選擇食材',
                html: `
                    <div id="ingredientsList">
                        ${ingredients.map(ingredient => `
                            <div>
                                <label for="ingredient_${ingredient.ingredientId}">${ingredient.ingredientName}:</label>
                                <input id="ingredient_${ingredient.ingredientId}" type="number" class="swal2-input" placeholder="數量" />
                            </div>
                        `).join('')}
                    </div>
                `,
                showCancelButton: true,
                cancelButtonText: '取消',
                confirmButtonText: '完成',
                preConfirm: () => {
                    let selectedIngredients = {}
                    ingredients.forEach(ingredient => {
                        const ingredientQuantity = document.getElementById(`ingredient_${ingredient.ingredientId}`).value
                        if (ingredientQuantity) {
                            selectedIngredients[ingredient.ingredientName] = Number(ingredientQuantity)
                        }
                    })
                    return selectedIngredients
                },
            })

            if (ingredientDetails) {
                // 創建商品物件並包含食材資訊
                const newProduct = {
                    productName,
                    price,
                    category,
                    photo,
                    ingredientDetails
                }

                // 發送請求新增商品
                try {
                    await axiosapi.post('/products', newProduct)
                    Swal.fire({
                        icon: 'success',
                        title: '成功',
                        text: '商品已成功新增',
                    })
                    getAllProducts() // 新增成功後重新獲取商品列表
                } catch (error) {
                    Swal.fire({
                        icon: 'error',
                        title: '錯誤',
                        text: '無法新增商品，請稍後再試。',
                    })
                }
            }
        }
    } catch (error) {
        Swal.fire({
            icon: 'error',
            title: '錯誤',
            text: '無法加載食材資料，請稍後再試。',
        })
    }
}

// 頁面加載時獲取所有商品
onMounted(() => {
    getAllProducts()
})
</script>

<style scoped>
.products-container {
    padding: 20px;
    width: 80%;
    margin: auto;
}

.search {
    margin-right: 20px;
    border: 1px solid gray;
    border-radius: 5px;
    text-align: center;
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
    margin-right: 20px;
    padding: 8px;
    font-size: 16px;
    width: 200px;
    border-radius: 5px;
    border: 1px solid #ccc;
}

.products-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 20px;
}

.products-table th,
.products-table td {
    padding: 10px;
    text-align: center;
    border: 1px solid #ddd;
}

.products-table th {
    background-color: #f4f4f4;
}

.add-product-btn {
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

.add-product-btn:hover {
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