import {Refueling} from "../mocks/refuelings.ts";
import {useState} from "react";

interface RefuelingTableProps {
    isAdmin: boolean;
    refuelings: Refueling[];
}

const RefuelingTable: React.FC<RefuelingTableProps> = ({isAdmin, refuelings}) => {

    const [sortColumn, setSortColumn] = useState<keyof Refueling | null>(null);
    const [sortDirection, setSortDirection] = useState<"asc" | "desc">("asc");

    const handleSort = (column: keyof Refueling) => {
        if (sortColumn === column) {
            setSortDirection(sortDirection === "asc" ? "desc" : "asc");
        } else {
            setSortColumn(column);
            setSortDirection("asc");
        }
    }

    const sortedRefuelings = [...refuelings].sort((a, b) => {
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
                    className="py-3 px-6 text-left cursor-pointer"
                    onClick={() => handleSort("timestamp")}
                >
                    Data
                </th>
                <th
                    className="py-3 px-6 text-right cursor-pointer"
                    onClick={() => handleSort("price")}
                >
                    Cena/l
                </th>
                <th
                    className="py-3 px-6 text-right cursor-pointer"
                    onClick={() => handleSort("amount")}
                >
                    Ilość (l)
                </th>
                <th
                    className="py-3 px-6 text-right cursor-pointer"
                    onClick={() => handleSort("totalAmount")}
                >
                    Całkowita kwota
                </th>
                <th
                    className="py-3 px-6 text-right cursor-pointer"
                    onClick={() => handleSort("mileageAtTime")}
                >
                    Przebieg
                </th>

                {
                    isAdmin && (
                        <th className="py-3 px-6 text-center">Usuń</th>
                    )
                }
            </tr>
            </thead>
            <tbody className="text-gray-700 text-sm font-light">
            {
                sortedRefuelings.map((refueling, index) => (
                    <tr
                        key={index}
                    >
                        <td className="py-3 px-6 text-left">{refueling.timestamp}</td>
                        <td className="py-3 px-6 text-right">{refueling.price}</td>
                        <td className="py-3 px-6 text-right">{refueling.amount}</td>
                        <td className="py-3 px-6 text-right">{refueling.totalAmount}</td>
                        <td className="py-3 px-6 text-right">{refueling.mileageAtTime}</td>

                        {
                            isAdmin && (
                                <td className="py-3 px-6 text-center">
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

export default RefuelingTable;