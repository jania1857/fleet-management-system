import React, {useEffect, useState} from "react";
import {ChangeStatusRequest, UserDto, VehicleDto} from "../../../generated-client";
import {managerApi} from "../../../api/apiClient.ts";
import {isVehicleAssignable} from "../../../util/utils.ts";

export const NewAssignment = () => {

    const [loading, setLoading] = useState<boolean>(true);
    const [selectedVehicle, setSelectedVehicle] = useState<string>("");
    const [selectedDriver, setSelectedDriver] = useState<string>("");
    const [vehicles, setVehicles] = useState<VehicleDto[]>([]);
    const [drivers, setDrivers] = useState<UserDto[]>([]);

    useEffect(() => {
        managerApi.getAllVehicles()
            .then((response) => {
                setVehicles(response.data);
            })
            .catch((error) => {
                console.error("Błąd podczas pobierania pojazdów: ", error);
            });

        managerApi.getDrivers()
            .then((response) => {
                setDrivers(response.data);
            })
            .catch((error) => {
                console.error("Błąd podczas pobierania kierowców: ", error);
            })
            .finally(() => setLoading(false));
    }, []);

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        if (selectedVehicle === "") {
            alert("Wybierz pojazd z listy");
            return;
        }
        if (selectedDriver === "") {
            alert("Wybierz kierowcę z listy");
            return;

        }

        const userId = +selectedDriver;
        const vehicleId = +selectedVehicle;

        await managerApi.newAssignment(userId, vehicleId)
            .then(async (response) => {
                if (response.status === 200) {
                    const request: ChangeStatusRequest = {
                        "newStatus": "ASSIGNED"
                    }
                    await managerApi.changeStatusForVehicle(vehicleId, request)
                        .then((response) => {
                            if (response.status === 200) {
                                alert("Pomyślnie przypisano pojazd do kierowcy");
                                window.location.reload();
                            }
                        })
                        .catch((error) => {
                            alert("Błąd podczas zmiany statusu pojazdu: " + error);
                        });
                }
            })
            .catch((error) => {
                alert("Błąd podczas przypisywania pojazdu do kierowcy: " + error);
            });
    }

    if (loading) {
        return <p>Ładowanie...</p>
    }

    return (
        <form
            onSubmit={handleSubmit}
            className="px-6 bg-white border border-gray-200 rounded-lg shadow-md max-w-3xl mx-auto py-2"
        >
            <h2 className="text-2xl font-bold mb-4">Przypisz pojazd do kierowcy</h2>

            <div className="mb-4">
                <label className="block text-gray-700 font-bold mb-2">Pojazd do przypisania</label>
                <select
                    className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                    value={selectedVehicle}
                    onChange={(e) => setSelectedVehicle(e.target.value)}
                >
                    <option value={""}>Wybierz pojazd z listy</option>
                    {
                        vehicles.map((vehicle) => isVehicleAssignable(vehicle) && (
                            <option
                                key={vehicle.id}
                                value={vehicle.id}
                            >
                                {vehicle.manufacturer} {vehicle.model} [ID: {vehicle.id}]
                            </option>
                        ))
                    }
                </select>
            </div>

            <div className="mb-4">
                <label className="block text-gray-700 font-bold mb-2">Kierowca do przypisania</label>
                <select
                    className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                    value={selectedDriver}
                    onChange={(e) => setSelectedDriver(e.target.value)}
                >
                    <option value={""}>Wybierz kierowcę z listy</option>
                    {
                        drivers.map((driver) => (
                            <option
                                key={driver.id}
                                value={driver.id}
                            >
                                {driver.firstname} {driver.lastname} [ID: {driver.id}]
                            </option>
                        ))
                    }
                </select>
            </div>

            <button
                type="submit"
                className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
                Przypisz
            </button>

        </form>
    );
};