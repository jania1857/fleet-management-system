import React, {useState} from "react";
import {NewInsuranceRequestInsuranceTypeEnum} from "../../../../generated-client";
import {managerApi} from "../../../../api/apiClient.ts";

interface ServiceFormProps {
    vehicleId: number;
    mileage: number | undefined;
}

const ServiceForm: React.FC<ServiceFormProps> = ({ vehicleId, mileage }) => {
    const [serviceName, setServiceName] = useState("");
    const [serviceDescription, setServiceDescription] = useState("");
    const [mileageAtTheTime, setMileageAtTheTime] = useState<number>(mileage === undefined ? 0 : mileage);
    const [serviceCost, setServiceCost] = useState(0);

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        const confirm = window.confirm(`Czy na pewno chcesz wprowadzić nowe ubezpieczenie?`);
        if (!confirm) return;

        await managerApi.newService(vehicleId, {
            name: serviceName,
            description: serviceDescription,
            cost: serviceCost,
            mileageAtTheTime: mileageAtTheTime,
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
                    <label className="block text-gray-700 font-bold mb-2">Wykonany serwis</label>
                    <input
                        type="text"
                        className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                        value={serviceName}
                        onChange={(e) => setServiceName(e.target.value)}
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700 font-bold mb-2">Szczegóły</label>
                    <textarea
                        value={serviceDescription}
                        onChange={(e) => (setServiceDescription(e.target.value))}
                        className="w-full h-48 px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700 font-bold mb-2">Przebieg przy serwisie</label>
                    <input
                        type="number"
                        className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                        value={mileageAtTheTime}
                        min={0}
                        onChange={(e) => setMileageAtTheTime(Number(e.target.value))}
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700 font-bold mb-2">Cena</label>
                    <input
                        type="number"
                        className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                        value={serviceCost}
                        min={0}
                        onChange={(e) => setServiceCost(Number(e.target.value))}
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

export default ServiceForm