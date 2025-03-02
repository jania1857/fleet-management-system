import React, {useState} from "react";
import {managerApi} from "../../../../api/apiClient.ts";

interface InspectionFormProps {
    vehicleId: number;
}

const InspectionForm: React.FC<InspectionFormProps> = ({ vehicleId }) => {

    const [inspectionDate, setInspectionDate] = useState("");
    const [inspectionDescription, setInspectionDescription] = useState("");
    const [inspectionPassed, setInspectionPassed] = useState(false);

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        console.log(inspectionDate);
        const confirm = window.confirm(`Czy na pewno chcesz wprowadzić nowy przegląd techniczny?`);
        if (!confirm) return;

        await managerApi.newInspection(vehicleId, {
            inspectionDate: inspectionDate + "T00:00:00.000Z",
            description: inspectionDescription,
            passed: inspectionPassed
        }).then(async (response) => {
            if (response.status === 200) {
                alert("Pomyślnie wprowadzono przegląd dla pojazdu")
            }
        }).catch((e) => {
            alert("Błąd podczas wprowadzania przeglądu dla pojazdu: " + e)
        })

        window.location.reload();
    }

    return (
        <>
            <hr className="mt-5 mb-5 border-gray-800"/>
            <form
                onSubmit={handleSubmit}
                className="px-6 bg-white border border-gray-200 rounded-lg shadow-md max-w-3xl mx-auto py-4"
            >
                <h2 className="text-2xl font-bold mb-4">Nowy przegląd</h2>

                <div className="mb-4">
                    <label className="block text-gray-700 font-bold mb-2">Data</label>
                    <input
                        type="date"
                        value={inspectionDate}
                        onChange={(e) => setInspectionDate(e.target.value)}
                        className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                        required
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700 font-bold mb-2">Szczegóły</label>
                    <textarea
                        value={inspectionDescription}
                        onChange={(e) => setInspectionDescription(e.target.value)}
                        className="w-full h-48 px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>

                <div className="mb-4">
                    <label className="flex text-gray-700 font-bold mb-2">
                        <input
                            type="checkbox"
                            checked={inspectionPassed}
                            onChange={(e) => setInspectionPassed(e.target.checked)}
                            className="mr-2"
                        />
                        Rozpatrzony pozytywnie
                    </label>
                </div>

                <button
                    type="submit"
                    className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                    Wprowadź przegląd
                </button>
            </form>
        </>
    )
}

export default InspectionForm