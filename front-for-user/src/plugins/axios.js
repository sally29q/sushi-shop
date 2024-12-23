import axios from "axios";

const instance = axios.create({
    baseURL: import.meta.env.VITE_API_URL,
    timeout: 5000,
});

instance.interceptors.response.use(
    function (response) {
        return response;
    },
    function (error) {
        if (error.response && error.response.status === 403) {
            window.location.href = "/403";
        }
        return Promise.reject(error);
    }
);

export default instance;
