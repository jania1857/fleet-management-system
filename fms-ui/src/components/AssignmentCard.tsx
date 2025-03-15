import React from "react";
import {UserAssignmentResponse} from "../generated-client";
import {formatDate} from "../util/utils.ts";

interface AssignmentCardProps {
    assignment: UserAssignmentResponse,
    endedHidden: boolean,
    setActivePopup: (visible: number) => void;
    setPopupVehicleId: (visible: number) => void;
}

const AssignmentCard: React.FC<AssignmentCardProps> = ({ assignment, endedHidden, setActivePopup, setPopupVehicleId }) => {

    const assignedVehicle = assignment.vehicle;

    if (!assignment || typeof assignment === "undefined" || !assignedVehicle) {
        return <div>
            "Brak informacji o przypisaniu"
        </div>
    }

    const title = assignedVehicle.manufacturer + " " + assignedVehicle.model;

    if (assignment.endDate !== "" && assignment.endDate != null && !endedHidden) return;

    return (
        <div className="p-4 bg-gray-100 border border-gray-200 rounded-lg shadow-md max-w-3xl py-4 w-[300px]">
            <h3 className="text-xl font-bold mb-4">{title}</h3>
            <div className="text-lg text-left">Początek: {formatDate(assignment.startDate)}</div>
            <div className="text-lg text-left">Koniec: {formatDate(assignment.endDate)}</div>
            {
                (assignment.endDate === "" || assignment.endDate == null) && (
                    <div className="mt-2">
                        <button
                            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 w-[200px] mb-2"
                            onClick={() => {
                                setActivePopup(0);
                                setPopupVehicleId((assignment.vehicle && assignment.vehicle.id != undefined) ? assignment.vehicle.id : -1);
                            }}
                        >
                            Ręczne tankowanie
                        </button>
                        <button
                            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 w-[200px]"
                            onClick={() => {
                                setActivePopup(1);
                                setPopupVehicleId((assignment.vehicle && assignment.vehicle.id != undefined) ? assignment.vehicle.id : -1);
                            }}
                        >
                            Wprowadź przegląd
                        </button>
                    </div>
                )
            }

        </div>
    )
}

export default AssignmentCard;