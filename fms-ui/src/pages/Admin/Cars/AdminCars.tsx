import React, {useEffect, useState} from "react";
import {VehicleDto} from "../../../generated-client";
import {managerApi} from "../../../api/apiClient.ts";
import VehiclesTable from "../../../components/VehiclesTable.tsx";
import {useNavigate} from "react-router-dom";

const AdminCars: React.FC = () => {

    const navigate = useNavigate();

    const [vehicles, setVehicles] = useState<VehicleDto[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        managerApi.getAllVehicles()
            .then((response => {
                setVehicles(response.data);
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
                <h1 className="text-left text-3xl">Lista pojazdów</h1>
                <button
                    className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
                    onClick={() => navigate("new")}
                >
                    Dodaj pojazd
                </button>
            </div>
            <VehiclesTable isAdmin={true} vehicles={vehicles}/>
        </>
    )
}

export default AdminCars;