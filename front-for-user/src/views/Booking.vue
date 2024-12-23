<template>
    <div class="booking-form">
        <h2 style="text-align: center;">預訂訂單</h2>
        <form @submit.prevent="submitOrder">
            <!-- 手機號碼 -->
            <div class="form-group d-flex justify-content-center align-items-center">
                <label for="userPhone" style="font-size: 20px; font-weight: 400; margin-top: 7px;">手機號碼：</label>
                <input type="text" id="userPhone" v-model="order.userPhone" required
                    style="width: 300px; margin-left: 10px;" />
            </div>
            <!-- 內用或外帶 -->
            <div class="form-group d-flex justify-content-center align-items-center">
                <label for="orderType" class="form-label"
                    style="font-size: 20px; font-weight: 400; margin-top: 20px;">請選擇要內用還是外帶</label>
                <div class="order-type-buttons">
                    <label class="order-button">
                        <input type="radio" id="dineIn" value="內用" v-model="order.orderType" required />
                        <span class="button-text">內用</span>
                    </label>
                    <label class="order-button">
                        <input type="radio" id="takeOut" value="外帶" v-model="order.orderType" required />
                        <span class="button-text">外帶</span>
                    </label>
                </div>
            </div>
            <!-- 自動填入商品資訊 -->
            <div class="order-details">
                <h4>訂單詳情</h4>
                <ul>
                    <li v-for="(item, index) in orderDetails" :key="index">
                        <div>{{ item.productName }}</div>
                        <div style="margin-bottom: 10px;">數量：{{ item.quantity }} ・ 價格：${{ item.price }} ・ 小計：${{
                            item.quantity * item.price }}</div>
                    </li>
                </ul>
                <div>總金額：${{ totalPrice }}</div>
            </div>
            <div class="d-flex justify-content-center align-items-center">
                <button type="submit" class="btn btn-primary">提交訂單</button>
            </div>
        </form>
    </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute } from 'vue-router';
import { useRouter } from 'vue-router';
import axiosapi from "../plugins/axios.js";
import Swal from 'sweetalert2';

const order = ref({
    userPhone: '',
    orderType: '',
});

const router = useRouter();
const orderDetails = ref([]);

const totalPrice = computed(() =>
    orderDetails.value.reduce((total, item) => total + item.price * item.quantity, 0)
);

// 获取选中的商品信息
const route = useRoute();
onMounted(() => {
    const orderDetailsStr = localStorage.getItem('orderDetails');
    if (orderDetailsStr) {
        orderDetails.value = JSON.parse(orderDetailsStr);
        console.log("orderDetails", orderDetails.value); // 检查数据
    }
});

// 提交訂單
const submitOrder = async () => {
    try {
        const result = await Swal.fire({
            title: '確認訂單',
            text: '您確定要提交訂單嗎？',
            icon: 'question',
            showCancelButton: true,
            confirmButtonText: '確定',
            cancelButtonText: '取消'
        });

        if (result.isConfirmed) {
            // 生成 orderDetails 數據
            const formattedOrderDetails = orderDetails.value.map(item => ({
                productName: item.productName,
                quantity: item.quantity,
                price: item.price,
                subtotal: item.quantity * item.price,
            }));

            const orderData = {
                userPhone: order.value.userPhone,
                orderType: order.value.orderType,
                totalPrice: totalPrice.value,
                orderDetails: formattedOrderDetails,
            };

            // 將 orderData 轉換為 JSON 字串並保存到 localStorage
            localStorage.setItem('orderData', JSON.stringify(orderData));

            // 提交數據
            const response = await axiosapi.post('/orders', {
                userPhone: order.value.userPhone,
                orderType: order.value.orderType,
                totalPrice: totalPrice.value,
                orderStatus: 0,
                orderDetails: formattedOrderDetails,
            });

            if (response.status === 201) {
                // 跳轉到訂單完成頁面
                router.push('/order-success');
            }
        }
    } catch (error) {
        console.error('創建訂單失敗', error);
    }
};
</script>

<style scoped>
.booking-form {
    width: 35%;
    margin: 0 auto;
    padding: 20px;
    background: #f8f8f8;
    border-radius: 8px;
    margin-top: 50px;
}

.form-group {
    margin-bottom: 15px;
}

input {
    width: 100%;
    padding: 8px;
    margin-top: 5px;
}

.order-details {
    margin-top: 20px;
}

button {
    margin-top: 20px;
}

.order-type-buttons {
    display: flex;
    justify-content: space-around;
    margin-top: 10px;
}

.order-button {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    background-color: #ccc;
    cursor: pointer;
    transition: background-color 0.3s, transform 0.2s, border-color 0.3s;
    border: 2px solid transparent;
    margin-left: 20px;
    margin-right: 20px;
}

.order-button input[type="radio"] {
    display: none;
}

.order-button .button-text {
    font-size: 15px;
    color: #333;
    font-weight: bold;
    text-transform: uppercase;
    padding: 5px 10px;
}

.order-button input[type="radio"]:checked+.button-text {
    background-color: #007bff;
    color: #fff;
    transform: scale(1.1);
    border-color: #007bff;
}

.order-button:hover {
    background-color: #007bff;
    color: white;
    border-color: #007bff;
}

.order-button:hover .button-text {
    transform: scale(1.05);
}

.form-label {
    font-size: 18px;
    font-weight: bold;
    margin-bottom: 10px;
    display: block;
}
</style>