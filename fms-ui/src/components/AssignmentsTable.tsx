import {AssignmentDto, ChangeStatusRequest, UserDto, VehicleDto} from "../generated-client";
import React from "react";
import {managerApi} from "../api/apiClient.ts";

interface AssignmentsTableProps {
    assignments: AssignmentDto[];
    vehicles: VehicleDto[];
    drivers: UserDto[];
}

const AssignmentsTable: React.FC<AssignmentsTableProps> = ({assignments, vehicles, drivers}) => {


    const handleEndAssignment = async (assignmentId: number, vehicleId: number) => {
        if (!assignmentId || assignmentId === 0) {
            alert("Błąd podczas zakończenia przypisania: Brak ID przypisania");
            return;
        }
        if (!vehicleId || vehicleId === 0) {
            alert("Błąd podczas zakończenia przypisania: Brak ID pojazdu");
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
        <table className="min-w-full bg-white border border-gray-200 rounded-lg shadow-md">
            <thead className="bg-gray-100 text-gray-600 uppercase text-sm leading-normal">
                <tr>
                    <th className="py-3 px-6 text-right">
                        ID
                    </th>
                    <th className="py-3 px-6 text-right">
                        Początek
                    </th>
                    <th className="py-3 px-6 text-right">
                        Koniec
                    </th>
                    <th className="py-3 px-6 text-right">
                        Kierowca
                    </th>
                    <th className="py-3 px-6 text-right">
                        Samochód
                    </th>
                    <th className="py-3 px-6 text-right">
                        Akcje
                    </th>
                </tr>
            </thead>
            <tbody>
            {
                assignments.map((assignment: AssignmentDto) => (
                    <tr
                        key={assignment.id}
                    >
                        <td className="py-3 px-6 text-right">{assignment.id}</td>
                        <td className="py-3 px-6 text-right">{assignment.startTime}</td>
                        <td className="py-3 px-6 text-right">{assignment.endTime}</td>
                        <td className="py-3 px-6 text-right">{
                            drivers.find(driver => driver.id === assignment.userId)?.username + " " +
                            drivers.find(driver => driver.id === assignment.userId)?.lastname + ` [ID:${assignment.userId}]`
                        }</td>
                        <td className="py-3 px-6 text-right">{
                            vehicles.find(vehicle => vehicle.id === assignment.vehicleId)?.manufacturer + " " +
                            vehicles.find(vehicle => vehicle.id === assignment.vehicleId)?.model + ` [ID:${assignment.vehicleId}]`

                        }</td>
                        <td className="py-3 px-6 text-right">
                            {
                                (assignment.endTime === null || assignment.endTime === "") && (
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
    )
}

export default AssignmentsTable;