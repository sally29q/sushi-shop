import { defineStore } from 'pinia';

export const useCartStore = defineStore('cart', {
    state: () => ({
        cartItems: [] // 保存購物車的商品數組
    }),
    actions: {
        // 添加商品到購物車
        addToCart(product) {
            const existingItem = this.cartItems.find(item => item.productId === product.productId);
            if (existingItem) {
                existingItem.quantity = this.getQuantity(product.productId);
            } else {
                this.cartItems.push({ ...product, quantity: 1 }); // 如果是新商品，添加到購物車
            }
        },
        // 更新商品數量
        updateQuantity(productId, quantity) {
            const item = this.cartItems.find(item => item.productId === productId);
            if (item) {
                item.quantity = quantity;
            }
        },
        // 獲取購物車商品數量
        getQuantity(productId) {
            const item = this.cartItems.find(item => item.productId === productId);
            return item ? item.quantity : 0;
        },
        // 刪除購物車商品
        removeFromCart(productId) {
            this.cartItems = this.cartItems.filter(item => item.productId !== productId);
        }
    }
});
