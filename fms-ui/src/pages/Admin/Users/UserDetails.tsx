import React, {useEffect, useState} from "react";
import {ChangeStatusRequest, UserDto, VehicleDto} from "../../../generated-client";
import {useParams} from "react-router-dom";
import {adminApi, managerApi} from "../../../api/apiClient.ts";
import image from "../../../assets/placeholder.jpg";
import Accordion from "../../../components/Accordion.tsx";
import {isVehicleAssignable} from "../../../util/utils.ts";

const UserDetails: React.FC = () => {

    const [user, setUser] = useState<UserDto | null>(null);
    const [loading, setLoading] = useState(true);
    const [vehicles, setVehicles] = useState<VehicleDto[]>([])
    const [assignmentSelectVisible, setAssignmentSelectVisible] = useState<boolean>(false);
    const [selectedVehicleId, setSelectedVehicleId] = useState<number | null>(null);

    const {id} = useParams<{ id: string }>();
    if (!id) {
        return (
            <h1 className="text-5xl text-red-500">Nie przekazano ID użytkownika</h1>
        )
    }
    const userId: number = +id;

    useEffect(() => {
        adminApi.getAllUsers()
            .then((response) => {
                const foundUser = response.data.find(user => user.id === userId);
                setUser(foundUser || null);
            })
            .catch((error) => {
                console.error(error);
            })
            .finally(() => setLoading(false))

        managerApi.getAllVehicles()
            .then(response => {
                setVehicles(response.data);
            })
            .catch((error) => {
                console.error("Błąd podczas pobierania listy pojazdów: ", error);
            })
    }, [])

    if (loading) {

        return (
            <p>Ładowanie...</p>
        )
    }

    if (!user) {
        setLoading(false);
        return (
            <h1 className="text-5xl text-red-500">Nie znaleziono użytkownika o takim ID</h1>
        )
    }

    const handleAssignment = async () => {
        if (!selectedVehicleId) {
            alert("Wybierz pojazd do przypisania");
            return;
        }
        await managerApi.newAssignment(userId, selectedVehicleId)
            .then(async (response) => {
                if(response.status !== 200) return;

                const request: ChangeStatusRequest = {
                    newStatus: "ASSIGNED"
                }

                await managerApi.changeStatusForVehicle(selectedVehicleId, request)
                    .then((response) => {
                        if(response.status === 200) {
                            alert("Pomyślnie przypisano pojazd do kierowcy");
                        }

                    })
                    .catch((error) => {
                        alert("Błąd podczas zmiany statusu pojazdu: " + error);
                    })
            })
            .catch((error) => {
                alert("Błąd podczas przypisywania pojazdu: " + error);
            });
    }

    const handleEndAssignment = async (assignmentId: number, vehicleId: number) => {
        if (!assignmentId || assignmentId === 0) {
            alert("Błąd podczas zakończenia przypisania: Brak ID przypisania");
            return;
        }

        const confirm = window.confirm(`Czy na pewno chcesz zakończyć przypisanie tego pojazdu?`);
        if (!confirm) return;

        await managerApi.endAssignment(assignmentId)
            .then(async (response) => {
                if (response.status === 200) {
                    const request: ChangeStatusRequest = {
                        newStatus: "READY"
                    }
                    await managerApi.changeStatusForVehicle(vehicleId, request);

                    alert("Pomyślnie zakończono przypisanie");
                    window.location.reload();
                }
            })
            .catch((error) => {
                alert("Błąd podczas zakończenia przypisania: " + error);
            });
    }


    return (
        <>
            <h1 className="text-5xl text-left font-bold">{user.firstname} {user.lastname}</h1>
            <hr className="border-gray-800"/>
            <div className="text-left">
                <h3 className="text-xl text-left">ID: {user.id} | Login: {user.username}</h3>
            </div>
            <div className="flex text-left gap-20">
                <img
                    src={image}
                    alt=""
                    className="w-full max-w-sm rounded-lg border border-gray-200 shadow-md"
                />
                <table>
                    <tbody>
                    <tr>
                        <td className="font-bold">Imię:</td>
                        <td>{user.firstname}</td>
                    </tr>
                    <tr>
                        <td className="font-bold">Nazwisko:&nbsp;</td>
                        <td>{user.lastname}</td>
                    </tr>
                    <tr>
                        <td className="font-bold">Login:</td>
                        <td>{user.username}</td>
                    </tr>
                    <tr>
                        <td className="font-bold">Rola:</td>
                        <td>{user.role}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <hr className="mt-5 mb-5 border-gray-800"/>
            <div className="flex gap-10 mb-5">
                {
                    user.role === "DRIVER" && (
                        <>
                            <button
                                className={`bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded ${assignmentSelectVisible ? "bg-green-800" : ""}`}
                                onClick={() => setAssignmentSelectVisible(!assignmentSelectVisible)}
                            >
                                Nowe przypisanie
                            </button>
                        </>
                    )
                }
                {
                    assignmentSelectVisible && (
                        <>
                            <select
                                className="px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                                value={selectedVehicleId || ""}
                                onChange={(e) => setSelectedVehicleId(+e.target.value)}
                            >
                                <option value="">Wybierz pojazd</option>
                                {
                                    (
                                        vehicles.map((vehicle) => isVehicleAssignable(vehicle) && (
                                            <option key={vehicle.id} value={vehicle.id}>
                                                [ID:{vehicle.id}] {vehicle.manufacturer} {vehicle.model}
                                            </option>
                                        ))
                                    )
                                }
                            </select>
                            <button
                                className={`bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded`}
                                onClick={() => handleAssignment()}
                            >
                                Przypisz
                            </button>
                        </>
                    )
                }

            </div>
            {
                user.role === "DRIVER" && (
                    <hr className="mt-5 mb-5 border-gray-800"/>
                )
            }

            {
                user.role === "DRIVER" && (
                    <Accordion title="Przypisania kierowcy">
                        <table className="min-w-full bg-white border border-gray-200 rounded-lg shadow-md">
                            <thead>
                            <tr>
                                <th
                                    className="py-3 px-6 text-right cursor-pointer"
                                >
                                    ID
                                </th>
                                <th
                                    className="py-3 px-6 text-right cursor-pointer"
                                >
                                    Początek
                                </th>
                                <th
                                    className="py-3 px-6 text-right cursor-pointer"
                                >
                                    Koniec
                                </th>
                                <th
                                    className="py-3 px-6 text-right cursor-pointer"
                                >
                                    Przypisany samochód
                                </th>
                                <th
                                    className="py-3 px-6 text-right cursor-pointer"
                                >
                                    Akcje
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            {
                                user.assignments?.map((assignment) => (
                                    <tr key={assignment.id}>
                                        <td className="py-3 px-6 text-right">{assignment.id}</td>
                                        <td className="py-3 px-6 text-right">{assignment.startTime}</td>
                                        <td className="py-3 px-6 text-right">{assignment.endTime === "" ? "Aktywne" : assignment.endTime}</td>
                                        <td className="py-3 px-6 text-right">
                                            {vehicles.find((vehicle) => vehicle.id === assignment.vehicleId)?.manufacturer} {vehicles.find((vehicle) => vehicle.id === assignment.vehicleId)?.model} [ID:{assignment.vehicleId}]
                                        </td>
                                        <td className="py-3 px-6 text-right">
                                            {

                                                (assignment.endTime === "" || assignment.endTime === null) && (
                                                    <button
                                                        className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
                                                        onClick={() => handleEndAssignment(assignment.id || 0, assignment.vehicleId || 0)}
                                                    >
                                                        Zakończ
                                                    </button>
                                                )
                                            }
                                        </td>
                                    </tr>
                                ))
                            }
                            </tbody>
                        </table>
                    </Accordion>
                )
            }

        </>
    )
}

export default UserDetails;