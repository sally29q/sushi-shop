<template>
    <div class="logo-container">
        <img src="../../public/logo.jpg" alt="logo" class="logo">
    </div>
    <h2>歡迎來到訂餐頁面</h2>
    <h2>請點選『開始點餐』查看菜單</h2>
    <div class="choose">
        <button class="next" @click="goToMenu">開始點餐</button>
    </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

const goToMenu = () => {
    router.push('/menu');
};
</script>

<style scoped>
.logo-container {
    display: flex;
    justify-content: center;
    align-items: center;
}

.logo {
    width: 250px;
    height: 125px;
    margin-top: 50px;
    margin-bottom: 40px;
}

h2 {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 30px;
}

.choose {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 120px;
}

.next {
    font-size: 30px;
    background-color: white;
    border: 2px solid gray;
    border-radius: 10px;
    padding: 10px;
}
</style>
