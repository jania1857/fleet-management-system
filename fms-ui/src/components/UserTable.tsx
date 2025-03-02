import React from "react";
import {UserDto} from "../generated-client";
import {useNavigate} from "react-router-dom";
import {adminApi} from "../api/apiClient.ts";

interface UserTableProps {
    users: UserDto[];
    isAdmin: boolean;
    showIot: boolean;
}

const UserTable: React.FC<UserTableProps> = ({users, isAdmin, showIot}) => {

    const navigate = useNavigate();

    const navigateUri: string = isAdmin ? "/admin/users/" : "/manager/users/";

    const handleDelete = async (userID: number | undefined) => {
        if (!userID) {
            alert("Brak ID dla użytkownika")
            return;
        }
        const confirmDelete = window.confirm(`Czy na pewno chcesz usunąć użytkownika o ID: ${userID}?`);
        if (!confirmDelete) return;

        await adminApi.deleteUser(userID);
        alert(`Użytkownik o ID ${userID} został usunięty`)
        users = users.filter(user => user.id !== userID);
    }
    const handleEdit = async (userId: number | undefined) => {
        if (!userId) {
            alert("Brak ID dla pojazdu")
            return;
        }
        navigate('edit/' + userId);
    }

    return (
        <table className="min-w-full bg-white border border-gray-200 rounded-lg shadow-md">
            <thead className="bg-gray-100 text-gray-600 uppercase text-sm leading-normal">
            <tr>
                <th className="py-3 px-6 text-right">
                    ID
                </th>
                <th className="py-3 px-6 text-right">
                    Login
                </th>
                <th className="py-3 px-6 text-right">
                    Imię
                </th>
                <th className="py-3 px-6 text-right">
                    Nazwisko
                </th>
                <th className="py-3 px-6 text-right">
                    Rola
                </th>
                {
                    isAdmin && (
                        <th className="py-3 px-6 text-right">Akcje</th>
                    )
                }
            </tr>
            </thead>
            <tbody>
            {
                users.map((user: UserDto) => {
                    if ((!showIot && user.role !== "IOT") || (showIot)) {
                    return (
                        <tr
                            key={user.id}
                            onClick={() => navigate(`${navigateUri}${user.id}`)}
                        >
                            <td className="py-3 px-6 text-right">{user.id}</td>
                            <td className="py-3 px-6 text-right">{user.username}</td>
                            <td className="py-3 px-6 text-right">{user.firstname}</td>
                            <td className="py-3 px-6 text-right">{user.lastname}</td>
                            <td className="py-3 px-6 text-right">{user.role}</td>
                            {
                                <td className="py-3 px-6 text-right">
                                    <button
                                        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded mr-2"
                                        onClick={(event) => {
                                            event.stopPropagation();
                                            handleEdit(user.id).then(_ => {
                                                return
                                            })
                                        }}
                                    >
                                        Edytuj
                                    </button>
                                    <button
                                        className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
                                        onClick={(event) => {
                                            event.stopPropagation();
                                            handleDelete(user.id).then(_ => {
                                                return
                                            })
                                        }}
                                    >
                                        Usuń
                                    </button>
                                </td>
                            }
                        </tr>
                    )
                }})
            }
            </tbody>
        </table>
    )


}

export default UserTable;