import React, {useEffect, useState} from "react";
import image from "../../../assets/placeholder.jpg";
import {useParams} from "react-router-dom";
import {ChangeStatusRequest, UserDto, VehicleDto} from "../../../generated-client";
import {allApi, managerApi} from "../../../api/apiClient.ts";
import {getVehicleMileage, getVehicleStatus} from "../../../util/utils.ts";
import Accordion from "../../../components/Accordion.tsx";

const CarDetails: React.FC = () => {

    const [vehicle, setVehicle] = useState<VehicleDto>();
    const [loading, setLoading] = useState(true);
    const [assignmentSelectVisible, setAssignmentSelectVisible] = useState(false);
    const [assignable, setAssignable] = useState<boolean>(true);
    const [drivers, setDrivers] = useState<UserDto[]>([]);
    const [selectedDriverId, setSelectedDriverId] = useState<number | null>(null);

    const {id} = useParams<{ id: string }>();

    if (!id) {
        return (
            <h1 className="text-5xl text-red-500">Nie przekazano ID pojazdu</h1>
        )
    }
    const vehicleId: number = +id;

    useEffect(() => {
        allApi.getVehicle(vehicleId || 1)
            .then(response => {
                setVehicle(response.data);
            })
            .catch((error) => {
                return (
                    <h1 className="text-5xl text-red-500">[404] Brak pojazdu o podanym ID - ${error}</h1>
                )
            })
            .finally(() => setLoading(false));

        managerApi.getDrivers()
            .then(response => {
                setDrivers(response.data);
            })
            .catch((error) => {
                console.error("Błąd podczas pobierania listy kierowców: ", error);
            })
    }, []);
    useEffect(() => {
        if (vehicle) {
            const vehicleAssignments = vehicle.assignments || [];
            const isAssignable = vehicleAssignments.every((assignment) => assignment.endTime !== null);
            setAssignable(isAssignable);
        }
    }, [vehicle]);

    const handleAssignment = async () => {
        if (!selectedDriverId || selectedDriverId === 0) {
            alert("Najpierw wybierz kierowcę")
            return;
        }

        await managerApi.newAssignment(selectedDriverId, vehicleId)
            .then(async (response) => {
                if (response.status === 200) {
                    const request: ChangeStatusRequest = {
                        newStatus: "ASSIGNED"
                    }
                    await managerApi.changeStatusForVehicle(vehicleId, request);
                    alert("Pojazd został przypisany do kierowcy");
                    window.location.reload();
                }
            })
            .catch((error) => {
                alert("Błąd podczas przypisywania pojazdu: " + error);
            });
    }

    const handleEndAssignment = async (assignmentId: number) => {
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



    if (loading) {
        return (
            <p>Ładowanie...</p>
        )
    }
    if(!vehicle) {
        return (
            <h1 className="text-5xl text-red-500">[404] Brak pojazdu o podanym ID - ${id}</h1>
        )
    }


    return (
        <>
            <h1 className="text-5xl text-left font-bold">{vehicle.manufacturer} {vehicle.model} {vehicle.year}</h1>
            <hr className="border-gray-800"/>
            <div className="flex justify-between">
                <h3 className="text-xl text-left">ID: {vehicle.id} | VIN: {vehicle.vin}</h3>
                <h3 className="text-xl text-left">Aktualny przebieg: {getVehicleMileage(vehicle)?.newMileage} km</h3>
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
                        <td className="font-bold">Marka:</td>
                        <td>{vehicle.manufacturer}</td>
                    </tr>
                    <tr>
                        <td className="font-bold">Model:</td>
                        <td>{vehicle.model}</td>
                    </tr>
                    <tr>
                        <td className="font-bold">Rok produkcji:</td>
                        <td>{vehicle.year}</td>
                    </tr>
                    <tr>
                        <td className="font-bold">Nr rejestracyjny:</td>
                        <td>{vehicle.registrationNumber}</td>
                    </tr>
                    <tr>
                        <td className="font-bold">VIN:</td>
                        <td>{vehicle.vin}</td>
                    </tr>
                    <tr>
                        <td className="font-bold">Status:</td>
                        <td>{getVehicleStatus(vehicle)?.newStatus}</td>
                    </tr>
                    <tr>
                        <td className="font-bold">Rodzaj napędu:</td>
                        <td>{vehicle.fuelType}</td>
                    </tr>
                    <tr>
                        <td className="font-bold">Pojemność silnika:&nbsp;</td>
                        <td>{vehicle.displacement}</td>
                    </tr>
                    <tr>
                        <td className="font-bold">Przebieg:</td>
                        <td>{getVehicleMileage(vehicle)?.newMileage} km</td>
                    </tr>
                    </tbody>
                </table>

            </div>
            <hr className="mt-5 mb-5 border-gray-800"/>
            <div className="flex gap-10">
                {
                    assignable && (
                        <button
                            className={`bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded ${assignmentSelectVisible ? "bg-green-800" : ""}`}
                            onClick={() => setAssignmentSelectVisible(!assignmentSelectVisible)}
                        >
                            Nowe przypisanie
                        </button>
                    )
                }

                {
                    assignmentSelectVisible && (
                        <>
                            <select
                                className="px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                                value={selectedDriverId || ""}
                                onChange={(e) => setSelectedDriverId(+e.target.value)}
                            >
                                <option value="">Wybierz kierowcę</option>
                                {
                                    (
                                        drivers.map((driver) => (
                                            <option key={driver.id} value={driver.id}>
                                                [ID:{driver.id}] {driver.firstname} {driver.lastname}
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
            <hr className="mt-5 mb-5 border-gray-800"/>
            <Accordion title="Przypisania pojazdu">
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
                            Przypisany kierowca
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
                        vehicle.assignments?.map((assignment) => (
                            <tr key={assignment.id}>
                                <td className="py-3 px-6 text-right">{assignment.id}</td>
                                <td className="py-3 px-6 text-right">{assignment.startTime}</td>
                                <td className="py-3 px-6 text-right">{assignment.endTime === "" ? "Aktywne" : assignment.endTime}</td>
                                <td className="py-3 px-6 text-right">
                                    [ID:{assignment.userId}] {drivers.find((driver) => driver.id === assignment.userId)?.firstname} {drivers.find((driver) => driver.id === assignment.userId)?.lastname}
                                </td>
                                <td className="py-3 px-6 text-right">
                                    {

                                        (assignment.endTime === "" || assignment.endTime === null) && (
                                            <button
                                                className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
                                                onClick={() => handleEndAssignment(assignment.id || 0)}
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
        </>
    )
}

export default CarDetails;