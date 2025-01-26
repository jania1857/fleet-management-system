import {Vehicle} from "../mocks/cars.ts";
import {useState} from "react";
import {useNavigate} from "react-router-dom";

interface VehicleTableProps {
    isAdmin: boolean;
    vehicles: Vehicle[];
}

const VehicleTable: React.FC<VehicleTableProps> = ({isAdmin, vehicles}) => {

    const [sortColumn, setSortColumn] = useState<keyof Vehicle | null>(null);
    const [sortDirection, setSortDirection] = useState<"asc" | "desc">("asc");
    const navigate = useNavigate();

    const navigateUri: string = isAdmin ? "/admin/cars/" : "/manager/cars/";

    const handleSort = (column: keyof Vehicle) => {
        if (sortColumn === column) {
            setSortDirection(sortDirection === "asc" ? "desc" : "asc");
        } else {
            setSortColumn(column);
            setSortDirection("asc");
        }
    }

    const sortedVehicles = [...vehicles].sort((a, b) => {
        if (!sortColumn) return 0;
        const aValue = a[sortColumn];
        const bValue = b[sortColumn];

        if (typeof aValue === "number" && typeof bValue === "number") {
            return sortDirection === "asc" ? aValue - bValue : bValue - aValue;
        }

        if (typeof aValue === "string" && typeof bValue === "string") {
            return sortDirection === "asc"
                ? aValue.localeCompare(bValue)
                : bValue.localeCompare(aValue);
        }

        return 0;
    })

    return (
        <table className="min-w-full bg-white border border-gray-200 rounded-lg shadow-md">
            <thead className="bg-gray-100 text-gray-600 uppercase text-sm leading-normal">
            <tr>
                <th
                    className="py-3 px-6 text-right cursor-pointer"
                    onClick={ () => handleSort("id") }
                >
                    ID</th>
                <th
                    className="py-3 px-6 text-left cursor-pointer"
                    onClick={ () => handleSort("make") }
                >
                    Marka</th>
                <th
                    className="py-3 px-6 text-left cursor-pointer"
                    onClick={ () => handleSort("model") }
                >
                    Model</th>
                <th
                    className="py-3 px-6 text-right cursor-pointer"
                    onClick={ () => handleSort("year") }
                >
                    Rocznik</th>
                <th
                    className="py-3 px-6 text-right cursor-pointer"
                    onClick={ () => handleSort("registrationNumber") }
                >
                    Nr rej.</th>
                <th
                    className="py-3 px-6 text-right cursor-pointer"
                    onClick={ () => handleSort("vin") }
                >
                    VIN</th>
                <th
                    className="py-3 px-6 text-left cursor-pointer"
                    onClick={ () => handleSort("status") }
                >
                    Status</th>
                <th
                    className="py-3 px-6 text-left cursor-pointer"
                    onClick={ () => handleSort("fuel") }
                >
                    Paliwo</th>
                <th
                    className="py-3 px-6 text-right cursor-pointer"
                    onClick={ () => handleSort("displacement") }
                >
                    Poj. silnika</th>
                <th
                    className="py-3 px-6 text-right cursor-pointer"
                    onClick={ () => handleSort("mileage") }
                >
                    Przebieg</th>
                {
                    isAdmin && (
                        <th className="py-3 px-6 text-center">Akcje</th>
                    )
                }
            </tr>
            </thead>
            <tbody className="text-gray-700 text-sm font-light">
            {
                sortedVehicles.map((vehicle) => (
                    <tr
                        key={vehicle.id}
                        className={`
                            ${vehicle.status === "ACTIVE" ? "bg-green-100 hover:bg-green-200" : ""}
                            ${vehicle.status === "INACTIVE" ? "bg-red-100 hover:bg-red-200" : ""}
                            ${vehicle.status === "SERVICE" ? "bg-yellow-100 hover:bg-yellow-200" : ""}
                        `}
                        onClick={() => navigate(`${navigateUri}${vehicle.id}`)}
                    >
                        <td className="py-3 px-6 text-right">{vehicle.id}</td>
                        <td className="py-3 px-6 text-left">{vehicle.make}</td>
                        <td className="py-3 px-6 text-left">{vehicle.model}</td>
                        <td className="py-3 px-6 text-right">{vehicle.year}</td>
                        <td className="py-3 px-6 text-right">{vehicle.registrationNumber}</td>
                        <td className="py-3 px-6 text-right">{vehicle.vin}</td>
                        <td className="py-3 px-6 text-left">{vehicle.status}</td>
                        <td className="py-3 px-6 text-left">{vehicle.fuel}</td>
                        <td className="py-3 px-6 text-right">{vehicle.displacement}</td>
                        <td className="py-3 px-6 text-right">{vehicle.mileage}</td>
                        {
                            isAdmin && (
                                <td className="py-3 px-6 text-right">
                                    <button
                                        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
                                        Edytuj
                                    </button>
                                    <button className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded">
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

export default VehicleTable;