import React, {useEffect, useState} from "react";
import {allApi} from "../../api/apiClient.ts";
import {UserAssignmentResponse} from "../../generated-client";
import AssignmentCard from "../../components/AssignmentCard.tsx";
import RefuelingPopUp from "./PopUps/RefuelingPopUp.tsx";

const MyAssignments: React.FC = () => {

    const [assignments, setAssignments] = useState<UserAssignmentResponse[]>([])
    const [loading, setLoading] = useState(true);
    const [hideEnded, setHideEnded] = useState(false);
    const [activePopup, setActivePopup] = useState<number>(-1);
    const [popupVehicleId, setPopupVehicleId] = useState(-1);

    useEffect(() => {
        allApi.getMyAssignments()
            .then((response => {
                setAssignments(response.data);
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
        <div
            className="px-4"
        >
            <div>
                <label className="flex items-center cursor-pointer w-[250px]">
                    <div className="relative">
                        <input
                            type="checkbox"
                            checked={hideEnded}
                            onChange={() => setHideEnded(!hideEnded)}
                            className="sr-only peer"
                        />
                        <div className="w-11 h-6 bg-gray-300 rounded-full peer-checked:bg-blue-600 transition-all"></div>
                        <div
                            className="absolute top-1 left-1 w-4 h-4 bg-white rounded-full shadow-md transition-all peer-checked:translate-x-5"
                        ></div>
                    </div>
                    <span className="ml-3 text-gray-900">Pokaż zakończone</span>
                </label>
            </div>
            <div
                className="flex flex-row flex-wrap gap-3 mt-4"
            >
                {
                    (assignments.length > 0) && assignments.map((assignment: UserAssignmentResponse) => (
                        <AssignmentCard assignment={assignment} endedHidden={hideEnded} setPopupVehicleId={setPopupVehicleId} setActivePopup={setActivePopup} />
                    ))
                }
            </div>

            {
                activePopup === 0 && (
                    <RefuelingPopUp vehicleId={popupVehicleId} setVisible={setActivePopup} />
                )
            }
        </div>
    )
}

export default MyAssignments;