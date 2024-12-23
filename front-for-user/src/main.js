import { createApp } from 'vue';
import App from './App.vue';
import router from './routers/router.js';
import { createPinia } from 'pinia';
import './assets/styles/global.css';

const app = createApp(App);
app.use(router);
app.use(createPinia());
app.mount('#app');
