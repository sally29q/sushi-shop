<template>
    <div v-if="products && products.length > 0" class="container mt-4">
        <div class="row">
            <div class="col-md-2 mb-5" v-for="(product, index) in products" :key="product.productId">
                <div class="card">
                    <img v-if="product.photo" :src="`data:image/jpeg;base64,${product.photo}`" class="card-img"
                        alt="Product Image" />
                </div>
                <div class="card-body">
                    <h6 class="card-title">{{ product.productName }}</h6>
                    <p class="card-text">價格: ${{ product.price }}</p>
                    <!-- 完售狀態 -->
                    <div v-if="product.isSoldOut" class="text-danger text-center">完售</div>
                    <div v-else></div>
                    <!-- 数量選擇 -->
                    <div class="d-flex align-items-center">
                        <button class="btn btn-secondary" :disabled="product.isSoldOut"
                            @click="decreaseQuantity(product)">-</button>
                        <input type="text" class="card-text form-control mx-2" v-model="quantities[product.productId]"
                            readonly />
                        <button class="btn btn-secondary" :disabled="product.isSoldOut"
                            @click="increaseQuantity(product)">+</button>
                    </div>
                    <p class="card-text mt-2">選擇的數量: {{ quantities[product.productId] }}</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, watch } from "vue";
import { useCartStore } from "../stores/cart.js";
import Swal from "sweetalert2";

const props = defineProps({
    products: {
        type: Array,
        required: true,
        default: () => []
    }
});

// 使用 Pinia store 來保存數量
const cartStore = useCartStore();
const quantities = ref({});

// 初始化數量
watch(
    () => props.products,
    (newProducts) => {
        if (newProducts && newProducts.length > 0) {
            newProducts.forEach((product) => {
                quantities.value[product.productId] = cartStore.getQuantity(product.productId) || 0;
            });
        }
    },
    { immediate: true }
);


// 增加數量
const increaseQuantity = (product) => {
    quantities.value[product.productId] += 1;
    cartStore.updateQuantity(product.productId, quantities.value[product.productId]);
    cartStore.addToCart(product);
    Swal.fire({
        position: "bottom-end",
        text: "已新增數量，可至右上角購物車查看",
        showConfirmButton: false,
        timer: 500
    });
};

// 减少數量
const decreaseQuantity = (product) => {
    if (quantities.value[product.productId] > 0) {
        quantities.value[product.productId] -= 1;

        if (quantities.value[product.productId] === 0) {
            cartStore.removeFromCart(product.productId); // 移除購物車中的商品
        } else {
            cartStore.updateQuantity(product.productId, quantities.value[product.productId]);
        }
        Swal.fire({
            position: "bottom-end",
            text: "已扣除數量，可至右上角購物車查看",
            showConfirmButton: false,
            timer: 500
        });
    }
};
</script>

<style>
.col-md-2 {
    margin-left: 50px;
    margin-right: 50px;
}

.card {
    width: 200px;
    height: 200px;
    padding-top: 10px;
    border: none;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    border-radius: 50%;
    padding: 10px;
}

.card-title {
    text-align: center;
    margin-top: 10px;
}

.card-text {
    text-align: center;
}

.btn-success {
    margin-left: 45px;
}
</style>