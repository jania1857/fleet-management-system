import {
    Configuration,
    AdminControllerApi,
    AllControllerApi,
    ManagerControllerApi,
    IotControllerApi
} from "../generated-client";
import axios from 'axios';
import {BASE_PATH} from "../generated-client/base.ts";

const getToken = () => localStorage.getItem("token") || "";

const apiConfig = new Configuration({
    basePath: BASE_PATH,
    accessToken: getToken,
});

const customAxiosInstance = axios.create({
    baseURL: BASE_PATH,
    headers: {
        Authorization: `Bearer ${getToken()}`,
    }
});

customAxiosInstance.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            console.error("Nieautoryzowany dostęp. Możliwe wylogowanie użytkownika.");
        }
        return Promise.reject(error);
    }
);

export const adminApi = new AdminControllerApi(apiConfig, undefined, customAxiosInstance);
export const managerApi = new ManagerControllerApi(apiConfig, undefined, customAxiosInstance);
export const allApi = new AllControllerApi(apiConfig, undefined, customAxiosInstance);
export const iotApi = new IotControllerApi(apiConfig, undefined, customAxiosInstance);