import React, {useEffect, useState} from "react";
import {VehicleDto} from "../../../generated-client";
import {managerApi} from "../../../api/apiClient.ts";
import UserTable from "../../../components/UserTable.tsx";
import {useNavigate} from "react-router-dom";

const ManagerUsers: React.FC = () => {
    const [users, setUsers] = useState<VehicleDto[]>([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        managerApi.getDrivers()
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
                </div>

                <button
                    className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
                    onClick={() => navigate("new")}
                >
                    Dodaj użytkownika
                </button>
            </div>
            <UserTable isAdmin={false} users={users} showIot={false}/>
        </>
    )
}

export default ManagerUsers