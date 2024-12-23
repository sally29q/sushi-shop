<template>
    <div class="orders-container">
        <h1 style="text-align: center;">訂單管理</h1>
        <!-- 訂單列表 -->
        <table v-if="orders.length > 0" class="orders-table">
            <thead>
                <tr>
                    <th class="col-1">訂單ID</th>
                    <th class="col-2">用戶電話</th>
                    <th class="col-2">訂單類型</th>
                    <th class="col-1">總價</th>
                    <th class="col-2">訂單狀態</th>
                    <th class="col-2">創建時間</th>
                    <th class="col-2">操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="order in orders" :key="order.orderId">
                    <td>{{ order.orderId }}</td>
                    <td>{{ order.userPhone }}</td>
                    <td>{{ order.orderType }}</td>
                    <td>${{ order.totalPrice }}</td>
                    <td>{{ order.orderStatus === 0 ? '預訂完成，等待餐點中' : '已取餐' }}</td>
                    <td>{{ formatDate(order.createdAt) }}</td>
                    <td>
                        <button @click="viewOrderDetails(order.orderId)" class="edit-btn">查看詳情</button>
                        <button @click="deleteOrder(order.orderId)" class="delete-btn">刪除</button>
                    </td>
                </tr>
            </tbody>
        </table>
        <button class="order-page-btn" @click="fetchOrders" style="text-align: center;">刷新訂單列表</button>
        <button class="order-page-btn" @click="goHome">返回首頁</button>
        <!-- 顯示訂單詳情彈窗 -->
        <div v-if="showDetails" class="order-details-modal">
            <div class="modal-content">
                <span class="close-btn" @click="closeModal">&times;</span>
                <h2>訂單詳情</h2>
                <p>訂單ID: {{ selectedOrder.orderId }}</p>
                <p>用戶電話: {{ selectedOrder.userPhone }}</p>
                <p>訂單類型: {{ selectedOrder.orderType }}</p>
                <p>總價: {{ selectedOrder.totalPrice }}</p>
                <p>訂單狀態: {{ selectedOrder.orderStatus === 0 ? '預訂完成，等待餐點中' : '已取餐' }}</p>
                <p>創建時間: {{ formatDate(selectedOrder.createdAt) }}</p>

                <h3>訂單明細</h3>
                <table class="order-details-table">
                    <thead>
                        <tr>
                            <th class="col-2">商品名稱</th>
                            <th class="col-1">數量</th>
                            <th class="col-1">價格</th>
                            <th class="col-1">小計</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="detail in selectedOrder.orderDetails" :key="detail.productName">
                            <td>{{ detail.productName }}</td>
                            <td>{{ detail.quantity }}</td>
                            <td>{{ detail.price }}</td>
                            <td>{{ detail.subtotal }}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axiosapi from "@/plugins/axios.js"
import Swal from 'sweetalert2';
import { useRouter } from 'vue-router'

const router = useRouter()
// 返回首頁
const goHome = () => {
    router.push('/')  // 跳轉到首頁
}

// 訂單列表資料
const orders = ref([]);
const selectedOrder = ref(null);
const showDetails = ref(false);  // 控制彈窗顯示

// 格式化日期
const formatDate = (date) => {
    const d = new Date(date);
    return d.toLocaleString();
};

// 取得訂單列表
const fetchOrders = async () => {
    let loadingTimer;

    try {
        // 顯示 loading
        Swal.fire({
            title: '加載中...',
            text: '正在取得訂單資料，請稍候...',
            icon: 'info',
            allowOutsideClick: false,
            didOpen: () => {
                Swal.showLoading();
            }
        });

        const response = await axiosapi.get('/orders');
        orders.value = response.data;

        // 让 loading 至少显示 500 毫秒
        setTimeout(() => {
            Swal.close();
        }, 300);
    } catch (error) {
        console.error('無法加載訂單:', error);
        orders.value = [];
    }
};

// 查看訂單詳情
const viewOrderDetails = async (orderId) => {
    try {
        const response = await axiosapi.get(`/orders/${orderId}`);
        selectedOrder.value = response.data;
        showDetails.value = true;  // 顯示訂單詳情彈窗
    } catch (error) {
        console.error('無法加載訂單詳情:', error);
    }
};

// 關閉彈窗
const closeModal = () => {
    showDetails.value = false;  // 關閉訂單詳情彈窗
};

// 刪除訂單
const deleteOrder = async (orderId) => {
    // 顯示確認刪除的提示框
    const result = await Swal.fire({
        title: '確認刪除?',
        text: "您確定要刪除這個訂單嗎？這個操作無法恢復。",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: '確定刪除',
        cancelButtonText: '取消',
        reverseButtons: true // 反轉按鈕順序
    });

    if (result.isConfirmed) {
        try {
            const response = await axiosapi.delete(`/orders/${orderId}`);
            Swal.fire('刪除成功', response.data, 'success'); // 顯示刪除成功消息
            fetchOrders();  // 重新載入訂單列表
        } catch (error) {
            console.error('刪除訂單失敗:', error);
            Swal.fire('刪除失敗', '無法刪除該訂單', 'error'); // 顯示刪除失敗消息
        }
    } else {
        Swal.fire('已取消', '您取消了刪除操作', 'info'); // 顯示取消消息
    }
};


// 頁面加載時獲取訂單列表
onMounted(() => {
    fetchOrders();
});
</script>

<style scoped>
.orders-container {
    padding: 20px;
}

.orders-table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 20px;
    text-align: center;
}

.order-details-table {
    width: 60%;
    margin: auto;
}

.orders-table th,
.orders-table td {
    padding: 10px;
    text-align: center;
    border: 1px solid #ddd;
}

table {
    width: 100%;
    border-collapse: collapse;
}

th,
td {
    padding: 10px;
    text-align: left;
    border: 1px solid #ddd;
}

th {
    background-color: #f4f4f4;
}

h2 {
    margin-top: 20px;
}

.order-page-btn {
    display: block !important;
    width: 200px !important;
    margin: 20px auto !important;
    padding: 10px !important;
    background-color: #5cb85c !important;
    color: white !important;
    border: none !important;
    border-radius: 5px !important;
    cursor: pointer !important;
}

.order-page-btn:hover {
    background-color: #4cae4c !important;
}

/* Modal styles */
.order-details-modal {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    text-align: center;
    z-index: 1000;
}

.modal-content {
    background-color: white;
    padding: 20px;
    border-radius: 5px;
    width: 80%;
    max-width: 800px;
    overflow-y: auto;
}

.close-btn {
    position: absolute;
    margin-right: 10px;
    top: 10px;
    right: 10px;
    font-size: 24px;
    cursor: pointer;
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
