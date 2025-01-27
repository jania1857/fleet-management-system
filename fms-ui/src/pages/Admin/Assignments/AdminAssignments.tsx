import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import AssignmentsTable from "../../../components/AssignmentsTable.tsx";
import {AssignmentDto, UserDto, VehicleDto} from "../../../generated-client";
import {managerApi} from "../../../api/apiClient.ts";

const AdminAssignments: React.FC = () => {

    const navigate = useNavigate();

    const [loading, setLoading] = useState(true);
    const [assignments, setAssignments] = useState<AssignmentDto[]>([]);
    const [drivers, setDrivers] = useState<UserDto[]>([]);
    const [vehicles, setVehicles] = useState<VehicleDto[]>([]);

    useEffect(() => {
        managerApi.getAllAssignments()
            .then((response) => {
                setAssignments(response.data);
            })
            .catch((error) => {
                console.error("Błąd podczas pobierania przypisań: ", error);
            })
            .finally(() => setLoading(false));

        managerApi.getDrivers()
            .then((response) => {
                setDrivers(response.data);
            })
            .catch((error) => {
                console.error("Błąd podczas pobierania kierowców: ", error);
            })
            .finally(() => setLoading(false));

        managerApi.getAllVehicles()
            .then((response) => {
                setVehicles(response.data);
            })
            .catch((error) => {
                console.error("Błąd podczas pobierania pojazdów: ", error);
            })
            .finally(() => setLoading(false));
    }, []);

    if (loading) {
        return <p>Ładowanie...</p>
    }

    return (
        <>
            <div className="flex mb-5 justify-between">
                <h1 className="text-left text-3xl">Przypisania</h1>
                <button
                    className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
                    onClick={() => navigate("new")}
                >
                    Nowe przypisanie
                </button>
            </div>
            <AssignmentsTable assignments={assignments} vehicles={vehicles} drivers={drivers}/>
        </>
    )

}

export default AdminAssignments;