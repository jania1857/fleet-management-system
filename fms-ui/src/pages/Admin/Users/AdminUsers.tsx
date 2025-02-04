import React, {useEffect, useState} from "react";
import {VehicleDto} from "../../../generated-client";
import {adminApi} from "../../../api/apiClient.ts";
import UserTable from "../../../components/UserTable.tsx";
import {useNavigate} from "react-router-dom";

const AdminUsers: React.FC = () => {
    const [users, setUsers] = useState<VehicleDto[]>([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();
    const [showIot, setShowIot] = useState<boolean>(false);

    useEffect(() => {
        adminApi.getAllUsers()
            .then((response => {
                setUsers(response.data);
            }))
            .catch((error) => {
                console.error("Błąd podczas pobierania listy pojazdów: ", error);
            })
            .finally(() => setLoading(false));
    }, []);

    if (loading) {
        return <p>Ładowanie...</p>
    }

    return (
        <>
            <div className="flex mb-5 justify-between">
                <div className="flex">
                    <h1 className="text-left text-3xl">Lista użytkowników</h1>
                    <label className="ml-5">
                        <input
                            type="checkbox"
                            value={showIot.toString()}
                            onChange={() => setShowIot(!showIot)}
                        />
                        <h2>Pokaż IOT</h2>
                    </label>
                </div>

                <button
                    className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
                    onClick={() => navigate("new")}
                >
                    Dodaj użytkownika
                </button>
            </div>
            <UserTable isAdmin={true} users={users} showIot={showIot}/>
        </>
    )
}

export default AdminUsers