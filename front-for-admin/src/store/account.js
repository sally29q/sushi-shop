
import { defineStore } from "pinia";
import { ref } from "vue";

const useAccountStore = defineStore(
    "users",
    () => {
        const account = ref("");
        const isLoggedIn = ref(false);
        // const token = ref("");

        function setAccount(val) {
            account.value = val;
        }

        function setLogin(val) {
            isLoggedIn.value = val;
        }

        // function setToken(val) {
        //     console.log("setToken called with: ", val);
        //     token.value = val;
        //     axiosapi.defaults.headers.authorization = val ? `Bearer ${val}` : "";
        //     if (val) {
        //         console.log("Storing token in LocalStorage:", val);
        //         localStorage.setItem("token", val);
        //     } else {
        //         console.log("Removing token from LocalStorage");
        //         localStorage.removeItem("token");
        //     }
        // }


        async function logout() {
            try {
                // 清除本地 Token 和狀態
                // setToken(null);
                setAccount("");
                setLogin(false);
                localStorage.clear(); // 清空 localStorage (可視需求保留其他資料)
                console.log("已成功登出");
            } catch (error) {
                console.error("登出失敗", error);
            }
        }

        return {
            account,
            isLoggedIn,
            // token,
            setAccount,
            setLogin,
            // setToken,
            logout,
        };
    },
    {
        persist: {
            storage: localStorage, // 將存儲切換為 localStorage
            paths: ["account", "isLoggedIn"], // 保持需要的狀態
            // paths: ["account", "isLoggedIn", "token"], // 保持需要的狀態
        },
    }
);

export default useAccountStore;
