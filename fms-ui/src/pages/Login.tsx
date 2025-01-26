import React from "react";
import {useState} from "react";
import axios from "axios";

const Login: React.FC = () => {
    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);

    const handleLogin = async (event: React.FormEvent) => {
        event.preventDefault();
        setLoading(true);
        setError(null);

        try {
            const response = await axios.post("http://localhost:8080/api/v1/public/login", {
                username,
                password,
            });

            console.log("Zalogowano pomyślnie:", response.data);

            localStorage.setItem("token", response.data.token);
            localStorage.setItem("username", response.data.userData.username);
            localStorage.setItem("role", response.data.userData.role.toLowerCase());

            window.location.href = `/${response.data.userData.role.toLowerCase()}/dashboard`;
            console.log(response.data.userData);
        } catch (error: any) {
            setError(error.response?.data?.message || "Wystąpił błąd logowania. Spróbuj ponownie później");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-100">
            <div className="w-full max-w-md bg-white border border-gray-200 rounded-lg shadow-md p-6">
                <h2 className="text-2xl font-bold text-gray-700 mb-6 text-center">
                    Zaloguj się
                </h2>
                <form onSubmit={handleLogin}>
                    <div className="mb-4">
                        <label
                            htmlFor="email"
                            className="block text-sm font-semibold text-gray-700"
                        >
                            Login
                        </label>
                        <input
                            type="text"
                            id="login"
                            name="login"
                            placeholder="Login..."
                            className="w-full px-4 py-2 mt-2 text-gray-700 bg-gray-50 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </div>

                    <div className="mb-6">
                        <label
                            htmlFor="password"
                            className="block text-sm font-semibold text-gray-700"
                        >
                            Hasło
                        </label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            placeholder="Hasło..."
                            className="w-full px-4 py-2 mt-2 text-gray-700 bg-gray-50 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>

                    {error && (
                        <p className="text-red-500 text-sm mb-4 text-center">{error}</p>
                    )}

                    <div className="flex items-center justify-between">
                        <button
                            type="submit"
                            disabled={loading}
                            className={`w-full px-4 py-2 text-white bg-blue-500 rounded-lg hover:bg-blue-600 focus:outline-none focus:bg-blue-600 ${
                                loading && "opacity-50 cursor-not-allowed"
                            }`}
                        >
                            {loading ? "Logowanie..." : "Zaloguj się"}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default Login;