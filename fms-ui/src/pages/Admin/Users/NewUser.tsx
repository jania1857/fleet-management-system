import React, {useState} from "react";
import {CreateUserRequest, CreateUserRequestRoleEnum} from "../../../generated-client";
import {adminApi} from "../../../api/apiClient.ts";

const NewUser: React.FC = () => {
    const [firstname, setFirstname] = useState("");
    const [lastname, setLastname] = useState("");
    const [role, setRole] = useState<CreateUserRequestRoleEnum>("DRIVER");

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();

        const request: CreateUserRequest = {
            firstname,
            lastname,
            role
        }

        const response = await adminApi.createUser(request);
        const body = response.data;

        alert(`### Login: ${body.username} ### Hasło: ${body.generatedPassword} ###\nPrzekazać użytkownikowi w sposób bezpieczny`)

        clearForm();
    }

    const clearForm = () => {
        setFirstname("");
        setLastname("");
        setRole("DRIVER")
    }

    return (
        <>
            <div className="p-6 bg-white border border-gray-200 rounded-lg shadow-md max-w-md mx-auto">
                <h2 className="text-2xl font-bold mb-4">Dodaj nowego użytkownika</h2>
                <form onSubmit={handleSubmit}>
                    <div className="mb-4">
                        <label htmlFor="firstName" className="block text-gray-700 font-bold mb-2">
                            Imię
                        </label>
                        <input
                            id="firstName"
                            type="text"
                            value={firstname}
                            onChange={(e) => setFirstname(e.target.value)}
                            className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                            placeholder="Wprowadź imię"
                        />
                    </div>
                    <div className="mb-4">
                        <label htmlFor="lastName" className="block text-gray-700 font-bold mb-2">
                            Nazwisko
                        </label>
                        <input
                            id="lastName"
                            type="text"
                            value={lastname}
                            onChange={(e) => setLastname(e.target.value)}
                            className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                            placeholder="Wprowadź nazwisko"
                        />
                    </div>
                    <div className="mb-4">
                        <label htmlFor="role" className="block text-gray-700 font-bold mb-2">
                            Rola
                        </label>
                        <select
                            id="role"
                            value={role}
                            onChange={
                                (e) => {
                                    const roleString = e.target.value;
                                    switch (roleString) {
                                        case "DRIVER":
                                            setRole("DRIVER");
                                            break;
                                        case "MANAGER":
                                            setRole("MANAGER");
                                            break;
                                        case "ADMIN":
                                            setRole("ADMIN");
                                            break;
                                    }
                                }
                            }
                            className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                            <option value="DRIVER">DRIVER</option>
                            <option value="MANAGER">MANAGER</option>
                            <option value="ADMIN">ADMIN</option>
                        </select>
                    </div>
                    <div className="flex justify-end">
                        <button
                            type="submit"
                            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                            Dodaj użytkownika
                        </button>
                    </div>
                </form>
            </div>
        </>
    )
}

export default NewUser;