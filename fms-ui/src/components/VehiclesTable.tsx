import {useNavigate} from "react-router-dom";
import {VehicleDto} from "../generated-client";
import {getVehicleMileage, getVehicleStatus} from "../util/utils.ts";
import {managerApi} from "../api/apiClient.ts";
import React from "react";

interface VehicleTableProps {
    isAdmin: boolean;
    vehicles: VehicleDto[];
}

const VehiclesTable: React.FC<VehicleTableProps> = ({isAdmin, vehicles}) => {

    const navigate = useNavigate();

    const navigateUri: string = isAdmin ? "/admin/cars/" : "/manager/cars/";
    const handleDelete = async (vehicleId: number | undefined) => {
        if (!vehicleId) {
            alert("Brak ID dla pojazdu")
            return;
        }
        const confirmDelete = window.confirm(`Czy na pewno chcesz usunąć użytkownika o ID: ${vehicleId}?`);
        if (!confirmDelete) return;

        await managerApi.deleteVehicle(vehicleId);
        alert(`Użytkownik o ID ${vehicleId} został usunięty`)
        vehicles = vehicles.filter(vehicle => vehicle.id !== vehicleId);
    }

    return (
        <table className="min-w-full bg-white border border-gray-200 rounded-lg shadow-md">
            <thead className="bg-gray-100 text-gray-600 uppercase text-sm leading-normal">
            <tr>
                <th
                    className="py-3 px-6 text-right cursor-pointer"
                >
                    ID
                </th>
                <th
                    className="py-3 px-6 text-left cursor-pointer"
                >
                    Marka
                </th>
                <th
                    className="py-3 px-6 text-left cursor-pointer"
                >
                    Model
                </th>
                <th
                    className="py-3 px-6 text-right cursor-pointer"
                >
                    Rocznik
                </th>
                <th
                    className="py-3 px-6 text-right cursor-pointer"
                >
                    Nr rej.
                </th>
                <th
                    className="py-3 px-6 text-right cursor-pointer"
                >
                    VIN
                </th>
                <th
                    className="py-3 px-6 text-left cursor-pointer"
                >
                    Status
                </th>
                <th
                    className="py-3 px-6 text-left cursor-pointer"
                >
                    Paliwo
                </th>
                <th
                    className="py-3 px-6 text-right cursor-pointer"
                >
                    Poj. silnika
                </th>
                <th
                    className="py-3 px-6 text-right cursor-pointer"
                >
                    Przebieg
                </th>
                {
                    isAdmin && (
                        <th className="py-3 px-6 text-right">Akcje</th>
                    )
                }
            </tr>
            </thead>
            <tbody className="text-gray-700 text-sm font-light">
            {
                vehicles.map((vehicle) => (
                    <tr
                        key={vehicle.id}
                        className={`
                            ${getVehicleStatus(vehicle)?.timestamp === "ACTIVE" ? "bg-green-100 hover:bg-green-200" : ""}
                            ${getVehicleStatus(vehicle)?.timestamp === "INACTIVE" ? "bg-red-100 hover:bg-red-200" : ""}
                            ${getVehicleStatus(vehicle)?.timestamp === "SERVICE" ? "bg-yellow-100 hover:bg-yellow-200" : ""}
                        `}
                        onClick={() => navigate(`${navigateUri}${vehicle.id}`)}
                    >
                        <td className="py-3 px-6 text-right">{vehicle.id}</td>
                        <td className="py-3 px-6 text-left">{vehicle.manufacturer}</td>
                        <td className="py-3 px-6 text-left">{vehicle.model}</td>
                        <td className="py-3 px-6 text-right">{vehicle.year}</td>
                        <td className="py-3 px-6 text-right">{vehicle.registrationNumber}</td>
                        <td className="py-3 px-6 text-right">{vehicle.vin}</td>
                        <td className="py-3 px-6 text-left">{getVehicleStatus(vehicle)?.newStatus}</td>
                        <td className="py-3 px-6 text-left">{vehicle.fuelType}</td>
                        <td className="py-3 px-6 text-right">{vehicle.displacement}</td>
                        <td className="py-3 px-6 text-right">{getVehicleMileage(vehicle)?.newMileage}</td>
                        {
                            isAdmin && (
                                <td className="py-3 px-6 text-right">
                                    <button
                                        className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
                                        onClick={(event) => {
                                            event.stopPropagation();
                                            handleDelete(vehicle.id).then(_ => {
                                                return
                                            })
                                        }}
                                    >
                                        Usuń
                                    </button>
                                </td>
                            )
                        }
                    </tr>
                ))
            }
            </tbody>
        </table>
    )
}

export default VehiclesTable;