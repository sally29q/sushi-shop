import { createRouter, createWebHistory } from 'vue-router';
import Home from "@/views/Home.vue";
import Ingredients from "@/views/Ingredients.vue";
import Products from "../views/Product.vue";
import Orders from "../views/Orders.vue";
import AdminList from "../views/AdminList.vue";
import Login from "@/views/secure/Login.vue";
import useAccountStore from "@/store/account.js";

const routes = [
    {
        path: '/',
        name: 'Home-link',
        component: Home,
        meta: { requiresAuth: true },
    },
    {
        path: "/ingredients-list",
        name: "Ingredients-link",
        component: Ingredients,
        meta: { requiresAuth: true },
    },
    {
        path: "/products-list",
        name: "Products-link",
        component: Products,
        meta: { requiresAuth: true },
    },
    {
        path: "/orders-list",
        name: "Orders-Link",
        component: Orders,
        meta: { requiresAuth: true },
    },
    {
        path: "/admin-list",
        name: "AdminList-link",
        component: AdminList,
        meta: { requiresAuth: true },
    },
    {
        path: "/secure/login",
        name: "Login-link",
        component: Login,
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

// 加入全域路由守衛
// router.beforeEach((to, from, next) => {
//     const accountStore = useAccountStore(); // 使用 Pinia Store

//     // 檢查路由是否需要登入
//     if (to.meta.requiresAuth && !accountStore.isLoggedIn) {
//         // 未登入，跳轉到登入頁
//         next({ name: "Login-link" });
//     } else {
//         // 已登入或不需要登入，放行
//         next();
//     }
// });

export default router;