import { createRouter, createWebHistory } from 'vue-router';
import Home from '@/views/Home.vue';
import Menu from '@/views/Menu.vue';
import Booking from '../views/Booking.vue';
import OrderSuccess from '../views/OrderSuccess.vue';

const routes = [
    {
        path: '/',
        name: 'Home-link',
        component: Home,
    },
    {
        path: '/menu',
        name: 'Menu',
        component: Menu
    },
    {
        path: '/booking',
        name: 'booking',
        component: Booking,
        props: true,
    },
    {
        path: '/order-success',
        name: 'OrderSuccess',
        component: OrderSuccess,
        props: true,
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});
export default router;