import React from "react";
import {useNavigate} from "react-router-dom";
import {UserRole} from "../types/User.ts";

const Login: React.FC = () => {
    const navigate = useNavigate();

    const handleLogin = (role: UserRole) => {
        localStorage.setItem("user", JSON.stringify({role}));
        navigate(`/${role}/dashboard`);
    };

    return (
        <div className="flex items-center justify-center h-screen bg-gray-100">
            <div className="bg-white p-6 shadow-md rounded">
                <h1 className="text-xl mb-4">Login</h1>
                <button
                    className="block w-full mb-2 bg-blue-500 text-white py-2 rounded"
                    onClick={() => handleLogin("admin")}
                >
                    Login as Admin
                </button>
                <button
                    className="block w-full mb-2 bg-blue-500 text-white py-2 rounded"
                    onClick={() => handleLogin("manager")}
                >
                    Login as Manager
                </button>
                <button
                    className="block w-full mb-2 bg-blue-500 text-white py-2 rounded"
                    onClick={() => handleLogin("driver")}
                >
                    Login as Driver
                </button>
            </div>
        </div>
    );
};

export default Login;