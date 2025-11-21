import axios, { AxiosInstance, AxiosError } from 'axios';
import { ApiResponse } from './types';

const API_BASE_URL =
    process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080/api/v1';

class ApiClient {
    private client: AxiosInstance;

    constructor() {
        this.client = axios.create({
            baseURL: API_BASE_URL,
            headers: {
                'Content-Type': 'application/json',
            },
        });

        // Request interceptor to add token
        this.client.interceptors.request.use(
            (config) => {
                const token = this.getToken();
                if (token) {
                    config.headers.Authorization = `Bearer ${token}`;
                }
                return config;
            },
            (error) => {
                return Promise.reject(error);
            }
        );

        // Response interceptor for error handling
        this.client.interceptors.response.use(
            (response) => response,
            async (error: AxiosError<ApiResponse<null>>) => {
                if (error.response?.status === 401) {
                    // Token expired or invalid
                    this.removeToken();
                    if (typeof window !== 'undefined') {
                        window.location.href = '/login';
                    }
                }
                return Promise.reject(error);
            }
        );
    }

    private getToken(): string | null {
        if (typeof window !== 'undefined') {
            return localStorage.getItem('token');
        }
        return null;
    }

    private removeToken(): void {
        if (typeof window !== 'undefined') {
            localStorage.removeItem('token');
            localStorage.removeItem('user');
        }
    }

    public setToken(token: string): void {
        if (typeof window !== 'undefined') {
            localStorage.setItem('token', token);
        }
    }

    public async get<T>(url: string, params?: any): Promise<ApiResponse<T>> {
        const response = await this.client.get<ApiResponse<T>>(url, { params });
        return response.data;
    }

    public async post<T>(url: string, data?: any): Promise<ApiResponse<T>> {
        const response = await this.client.post<ApiResponse<T>>(url, data);
        return response.data;
    }

    public async put<T>(
        url: string,
        data?: any,
        config?: any
    ): Promise<ApiResponse<T>> {
        const response = await this.client.put<ApiResponse<T>>(
            url,
            data,
            config
        );
        return response.data;
    }

    public async delete<T>(url: string): Promise<ApiResponse<T>> {
        const response = await this.client.delete<ApiResponse<T>>(url);
        return response.data;
    }
}

export const apiClient = new ApiClient();
