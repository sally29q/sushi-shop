<template>
    <div>
        <!-- navbar -->
        <nav class="navbar navbar-expand-lg navbar-light bg-white">
            <div class="container-fluid d-flex justify-content-between align-items-center">
                <a class="navbar-brand" href="/"><img src="../../public/logo.jpg" alt="logo" class="logo"></a>
                <h1 class="nav mx-auto">菜單</h1>
                <!-- 購物車 -->
                <div style="width: 200px;">
                    <button class="btn btn-outline-dark" @click="toggleCartModal" style="margin-left: 70%;">
                        <i class="bi bi-basket"></i>
                    </button>
                </div>
            </div>
        </nav>
        <!-- 購物車彈窗 -->
        <div v-if="isCartModalOpen" class="cart-modal">
            <div class="modal-content">
                <h2 style="text-align: center;">購物車</h2>
                <div v-if="cartItems.length" style="margin-top: 15px;">
                    <div v-for="(item, index) in cartItems" :key="index"
                        class="d-flex justify-content-between align-items-center">
                        <p>{{ item.productName }} ${{ item.price }} x {{ item.quantity }}</p>
                        <div class="d-flex justify-content-between align-items-center">
                            <p>小計：${{ item.price * item.quantity }}</p>
                        </div>
                    </div>
                    <p>總金額: ${{ totalPrice }}</p>
                </div>
                <div v-else>
                    <p>購物車是空的，請加入商品</p>
                </div>
                <div class="d-flex justify-content-between align-items-center">
                    <button class="close" @click="toggleCartModal">關閉</button>
                    <button class="order" @click="goToBooking">預訂</button>
                </div>
            </div>
        </div>
        <!-- 分類菜單 -->
        <div class="type mt-4">
            <div class="col-md-1">
                <img src="../../public/img/01_all.png" alt="all" class="icons">
                <button type="button" class="btn btn-danger" @click="setCategory('')">全部</button>
            </div>
            <div class="col-md-1">
                <img src="../../public/img/02_sushi.png" alt="sushi" class="icons">
                <button type="button" class="btn btn-danger" @click="setCategory('壽司')">壽司</button>
            </div>
            <div class="col-md-1">
                <img src="../../public/img/03_caviar.png" alt="caviar" class="icons">
                <button type="button" class="btn btn-danger" @click="setCategory('軍艦')">軍艦</button>
            </div>
            <div class="col-md-1">
                <img src="../../public/img/04_roll.png" alt="sushi-roll" class="icons">
                <button type="button" class="btn btn-danger" @click="setCategory('壽司卷')">壽司卷</button>
            </div>
            <div class="col-md-1">
                <img src="../../public/img/05_fried.png" alt="fried" class="icons">
                <button type="button" class="btn btn-danger" @click="setCategory('炸物')">炸物</button>
            </div>
            <div class="col-md-1">
                <img src="../../public/img/06_set.png" alt="set" class="icons">
                <button type="button" class="btn btn-danger" @click="setCategory('套餐')">套餐</button>
            </div>
        </div>
        <!-- 商品區 -->
        <div class="container mt-4">
            <div class="row">
                <Products v-if="filteredProducts.length" :products="filteredProducts" />
            </div>
        </div>
    </div>
</template>

<script setup>

import { ref, onMounted, computed } from "vue";
import { useRouter } from 'vue-router';
import { useCartStore } from "../stores/cart.js";
import axiosapi from "../plugins/axios.js";
import Products from "../components/Products.vue";
import Swal from "sweetalert2";

const products = ref([]);
const ingredients = ref([]);
const selectedCategory = ref('');
const cartStore = useCartStore();
const isCartModalOpen = ref(false);
const router = useRouter();

// 列出所有商品
onMounted(async () => {
    try {
        // 獲取產品數據
        const productResponse = await axiosapi.get("/products");
        products.value = productResponse.data;
        console.log("products.value", products.value);

        // 獲取食材數據
        const ingredientResponse = await axiosapi.get("/ingredients");
        ingredients.value = ingredientResponse.data;

        // 檢查庫存，更新產品狀態
        updateProductStatus();

    } catch (error) {
        console.error("獲取數據失敗：", error);
    }
});

// 檢查庫存
const updateProductStatus = () => {
    products.value.forEach((product) => {
        product.isSoldOut = Object.entries(product.ingredientDetails).some(([ingredient, qty]) => {
            const stock = ingredients.value.find(item => item.ingredientName === ingredient)?.totalQuantity || 0;
            return stock < qty;
        });
    });
};


// 根據選取的分類篩選商品
const filteredProducts = computed(() => {
    if (selectedCategory.value) {
        return products.value.filter(product => product.category === selectedCategory.value);
    }
    return products.value; // 如果没有选择分类，显示所有商品
});
// 設置選擇的分類
const setCategory = (category) => {
    Swal.fire({
        title: '加載中...',
        text: `正在加載 ${category || '全部'} 分類，請稍等！`,
        icon: 'info',
        allowOutsideClick: false,
        showConfirmButton: false,
        didOpen: () => {
            Swal.showLoading();
        }
    });
    selectedCategory.value = category;
    setTimeout(() => {
        Swal.close();
    }, 500);
};

// 控制購物車彈窗顯示
const toggleCartModal = () => {
    isCartModalOpen.value = !isCartModalOpen.value;
};

// 獲取購物車商品
const cartItems = computed(() => cartStore.cartItems);

// 計算總金額
const totalPrice = computed(() => {
    return cartItems.value.reduce((total, item) => total + item.price * item.quantity, 0);
});

//跳轉到預訂頁面並傳遞選取的商品
const goToBooking = () => {
    Swal.fire({
        title: '確認要預訂嗎？',
        text: '確認後即將前往預訂頁面。',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: '確認預訂',
        cancelButtonText: '取消',
    }).then((result) => {
        if (result.isConfirmed) {
            const orderDetails = cartItems.value.map(item => ({
                productName: item.productName,
                price: item.price,
                quantity: item.quantity,
            }));

            // 将 orderDetails 存储到 localStorage
            localStorage.setItem('orderDetails', JSON.stringify(orderDetails));

            router.push({ name: 'booking' });
            Swal.fire('預訂成功！', '您已成功到預訂頁面。', 'success');
        }
    });
};
</script>

<style scoped>
.logo {
    width: 180px;
    height: 90px;
}

.cart-modal {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1050;
}

.modal-content {
    background: white;
    padding: 20px;
    border-radius: 10px;
    max-width: 500px;
    width: 100%;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.type {
    width: 100%;
    display: flex;
    justify-content: center;
    margin-bottom: 50px;
    background-color: #eee;
    padding-top: 20px;
    padding-bottom: 20px;
}

.col-md-1 {
    margin-left: 30px;
    margin-right: 30px;
}

.icons {
    margin-left: 35px;
    width: 50px;
    height: 50px;
}

.btn-danger {
    margin: 10px;
    border-radius: 30px;
    background-color: rgb(189, 42, 42);
    font-size: 20px;
    flex: 0 1 15%;
    min-width: 100px;
}

.close {
    border-radius: 5px;
}

.order {
    background-color: rgb(248, 44, 44);
    color: #eee;
    border-radius: 5px;
}
</style>