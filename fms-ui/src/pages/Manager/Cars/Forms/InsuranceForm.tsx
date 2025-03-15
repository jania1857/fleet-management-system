import React, {useState} from "react";
import {managerApi} from "../../../../api/apiClient.ts";
import {NewInsuranceRequestInsuranceTypeEnum} from "../../../../generated-client";

interface InsuranceFormProps {
    vehicleId: number;
}

const InsuranceForm: React.FC<InsuranceFormProps> = ({ vehicleId }) => {
    const [insuranceType, setInsuranceType] = useState<NewInsuranceRequestInsuranceTypeEnum>("OC");
    const [insuranceNumber, setInsuranceNumber] = useState("");
    const [insuranceDescription, setInsuranceDescription] = useState("");
    const [insurer, setInsurer] = useState("");
    const [insuranceStartDate, setInsuranceStartDate] = useState("");
    const [insuranceCost, setInsuranceCost] = useState<number>(0);

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        const confirm = window.confirm(`Czy na pewno chcesz wprowadzić nowe ubezpieczenie?`);
        if (!confirm) return;

        await managerApi.newInsurance(vehicleId, {
            insuranceType: insuranceType,
            number: insuranceNumber,
            description: insuranceDescription,
            insurer: insurer,
            startDate: insuranceStartDate + "T00:00:00.000Z",
            cost: insuranceCost
        }).then(async (response) => {
            if (response.status === 200) {
                alert("Pomyślnie wprowadzono ubezpieczenie dla pojazdu")
            }
        }).catch((e) => {
            alert("Błąd podczas wprowadzania ubezpieczenia dla pojazdu: " + e)
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
                    <label className="block text-gray-700 font-bold mb-2">Data początku ubezpieczenia</label>
                    <input
                        type="date"
                        className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                        value={insuranceStartDate}
                        onChange={(e) => setInsuranceStartDate(e.target.value)}
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700 font-bold mb-2">Ubezpieczyciel</label>
                    <input
                        type="text"
                        className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                        value={insurer}
                        onChange={(e) => setInsurer(e.target.value)}
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700 font-bold mb-2">Numer polisy</label>
                    <input
                        type="text"
                        className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                        value={insuranceNumber}
                        onChange={(e) => setInsuranceNumber(e.target.value)}
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700 font-bold mb-2">Typ ubezpieczenia</label>
                    <select
                        className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                        value={insuranceType}
                        onChange={(e) => {
                            switch (e.target.value) {
                                case "OC":
                                    setInsuranceType(NewInsuranceRequestInsuranceTypeEnum.Oc)
                                    break;
                                case "AC":
                                    setInsuranceType(NewInsuranceRequestInsuranceTypeEnum.Ac)
                                    break;
                            }
                        }}
                    >
                        <option value="OC">OC</option>
                        <option value="AC">AC</option>
                    </select>
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700 font-bold mb-2">Opis</label>
                    <textarea
                        value={insuranceDescription}
                        onChange={(e) => (setInsuranceDescription(e.target.value))}
                        className="w-full h-48 px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700 font-bold mb-2">Cena</label>
                    <input
                        type="number"
                        className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                        value={insuranceCost}
                        min={0}
                        onChange={(e) => setInsuranceCost(Number(e.target.value))}
                    />
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

export default InsuranceForm