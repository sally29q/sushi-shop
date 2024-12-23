<template>
    <div class="order-success">
        <h2>以下是您的訂單詳細資訊</h2>
        <div class="order-summary" v-if="orderData">
            <p>手機號碼：{{ orderData.userPhone }}</p>
            <p>訂單狀態：已預訂完成，請耐心等待餐點</p>
            <p>訂單類型：{{ orderData.orderType }}</p>
            <p>訂單內容：</p>
            <ul>
                <li v-for="(item, index) in orderData.orderDetails" :key="index">
                    <div>{{ item.productName }}</div>
                    <div style="margin-bottom: 10px;">數量：{{ item.quantity }} ・ 價格：${{ item.price }} ・ 小計：${{
                        item.quantity * item.price }}</div>
                </li>
            </ul>
            <p>總金額：${{ orderData.totalPrice }}</p>
        </div>
        <div class="actions">
            <button @click="goBack" class="btn btn-primary">返回首頁</button>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useCartStore } from '../stores/cart.js';

const orderData = ref(null);
const orderDetails = ref([]);
const totalPrice = computed(() =>
    orderDetails.value.reduce((total, item) => total + item.price * item.quantity, 0)
);

// 獲取訂單詳細資訊
const router = useRouter();
onMounted(() => {
    const savedOrderData = localStorage.getItem('orderData');
    if (savedOrderData) {
        orderData.value = JSON.parse(savedOrderData);
        console.log('加載的訂單數據：', orderData.value);
    } else {
        console.error('没有找到訂單數據');
    }
});

// 返回首頁
const goBack = () => {
    const cartStore = useCartStore();
    cartStore.cartItems = []; // 清空購物車
    router.push('/');
};
</script>

<style scoped>
.order-success {
    width: 35%;
    margin: 0 auto;
    padding: 20px;
    background: #f8f8f8;
    border-radius: 8px;
    text-align: center;
    margin-top: 50px;
}

.order-summary {
    margin-top: 20px;
    text-align: left;
}

.total-price {
    font-weight: bold;
    margin-top: 15px;
}

.actions {
    margin-top: 20px;
}
</style>
