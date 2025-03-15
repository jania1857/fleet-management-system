import React, {useState} from "react";
import {
    CreateVehicleRequest,
    CreateVehicleRequestFuelTypeEnum,
    CreateVehicleRequestInsuranceTypeEnum
} from "../../../generated-client";
import {managerApi} from "../../../api/apiClient.ts";
import {convertDateToDateTime} from "../../../util/utils.ts";

const NewCar: React.FC = () => {

    const [manufacturer, setManufacturer] = useState("");
    const [model, setModel] = useState("");
    const [year, setYear] = useState(2024);
    const [registrationNumber, setRegistrationNumber] = useState("");
    const [vin, setVin] = useState("");
    const [fuelType, setFuelType] = useState<CreateVehicleRequestFuelTypeEnum>("DIESEL");
    const [displacement, setDisplacement] = useState(0);
    const [mileage, setMileage] = useState(0);
    const [insuranceStart, setInsuranceStart] = useState("");
    const [insuranceEnd, setInsuranceEnd] = useState("");
    const [insuranceType, setInsuranceType] = useState<CreateVehicleRequestInsuranceTypeEnum>("OC");
    const [insuranceNumber, setInsuranceNumber] = useState("");
    const [insurer, setInsurer] = useState("");
    const [inspectionDate, setInspectionDate] = useState("");
    const [nextInspectionDate, setNextInspectionDate] = useState("");
    const [fuelCardNumber, setFuelCardNumber] = useState("");



    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();

        const request: CreateVehicleRequest = {
            manufacturer,
            model,
            year,
            registrationNumber,
            vin,
            fuelType,
            displacement,
            fuelCardNumber,
            mileage,
            insuranceStart: convertDateToDateTime(new Date(insuranceStart)),
            insuranceEnd: convertDateToDateTime(new Date(insuranceEnd)),
            insuranceType,
            insuranceNumber,
            insurer,
            inspectionDate: convertDateToDateTime(new Date(insuranceEnd)),
            nextInspectionDate: convertDateToDateTime(new Date(insuranceEnd))
        }

        await managerApi.createVehicle(request)
            .then((response) => alert("Samochód został pomyślnie utworzony\n###################################\nLoginIOT: " + response.data.iotUsername + "\nHasłoIOT: " + response.data.iotPassword))
            .catch(error => alert(`Podczas tworzenia pojazdu wystąpił problem: ${error}`))
            .finally(() => clearForm());


        console.log(request);

    }

    const clearForm = () => {
        setManufacturer("");
        setModel("");
        setYear(2024);
        setRegistrationNumber("");
        setVin("");
        setFuelType("DIESEL");
        setDisplacement(0);
        setMileage(0);
        setInsuranceStart("");
        setInsuranceEnd("");
        setInsuranceType("OC");
        setInsuranceNumber("");
        setInsurer("");
        setInspectionDate("");
        setNextInspectionDate("");
    }

    return (
        <>
            <form
                onSubmit={handleSubmit}
                className="px-6 bg-white border border-gray-200 rounded-lg shadow-md max-w-3xl mx-auto py-2"
            >
                <h2 className="text-2xl font-bold mb-4">Dodaj nowy samochód</h2>

                <div className="mb-4">
                    <label className="block text-gray-700 font-bold mb-2">Producent</label>
                    <input
                        type="text"
                        value={manufacturer}
                        onChange={(e) => setManufacturer(e.target.value)}
                        className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                        placeholder="Producent"
                        required
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700 font-bold mb-2">Model</label>
                    <input
                        type="text"
                        value={model}
                        onChange={(e) => setModel(e.target.value)}
                        className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                        placeholder="Model"
                        required
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700 font-bold mb-2">Rok produkcji</label>
                    <input
                        type="number"
                        value={year}
                        onChange={(e) => setYear(+e.target.value)}
                        className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                        placeholder="Rok produkcji"
                        required
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700 font-bold mb-2">Numer rejestracyjny</label>
                    <input
                        type="text"
                        value={registrationNumber}
                        onChange={(e) => setRegistrationNumber(e.target.value)}
                        className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                        placeholder="Numer rejestracyjny"
                        required
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700 font-bold mb-2">Numer VIN</label>
                    <input
                        type="text"
                        value={vin}
                        onChange={(e) => setVin(e.target.value)}
                        className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                        placeholder="Numer VIN"
                        required
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700 font-bold mb-2">Rodzaj paliwa/napędu</label>
                    <select
                        className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                        value={fuelType}
                        onChange={(e) => {
                            switch (e.target.value) {
                                case "GASOLINE":
                                    setFuelType(e.target.value)
                                    break;
                                case "DIESEL":
                                    setFuelType(e.target.value)
                                    break;
                                case "GASOLINE_HYBRID":
                                    setFuelType(e.target.value)
                                    break;
                                case "DIESEL_HYBRID":
                                    setFuelType(e.target.value)
                                    break;
                                case "LPG":
                                    setFuelType(e.target.value)
                                    break;
                                case "ELECTRIC":
                                    setFuelType(e.target.value)
                                    break;
                            }
                        }}
                        required
                    >
                        <option value="DIESEL">Diesel</option>
                        <option value="GASOLINE">Benzyna</option>
                        <option value="DIESEL_HYBRID">Hybryda (diesel)</option>
                        <option value="GASOLINE_HYBRID">Hybryda (benzyna)</option>
                        <option value="LPG">LPG</option>
                        <option value="ELECTRIC">Elektryczny</option>
                    </select>
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700 font-bold mb-2">Pojemność silnika</label>
                    <input
                        type="number"
                        value={displacement}
                        onChange={(e) => setDisplacement(+e.target.value)}
                        className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                        placeholder="Pojemność silnika (cc)"
                        required
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700 font-bold mb-2">Przebieg</label>
                    <input
                        type="number"
                        value={mileage}
                        onChange={(e) => setMileage(+e.target.value)}
                        className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                        placeholder="Przebieg (km)"
                        required
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700 font-bold mb-2">Początek ubezpieczenia</label>
                    <input
                        type="date"
                        value={insuranceStart}
                        onChange={(e) => setInsuranceStart(e.target.value)}
                        className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                        placeholder="Początek ubezpieczenia"
                        required
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700 font-bold mb-2">Koniec ubezpieczenia</label>
                    <input
                        type="date"
                        value={insuranceEnd}
                        onChange={(e) => setInsuranceEnd(e.target.value)}
                        className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                        placeholder="Koniec ubezpieczenia"
                        required
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
                                    setInsuranceType(e.target.value)
                                    break;
                                case "AC":
                                    setInsuranceType(e.target.value)
                                    break;
                            }
                        }}
                        required
                    >
                        <option value="OC">OC</option>
                        <option value="AC">AC</option>
                    </select>
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700 font-bold mb-2">Numer ubezpieczenia</label>
                    <input
                        type="text"
                        value={insuranceNumber}
                        onChange={(e) => setInsuranceNumber(e.target.value)}
                        className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                        placeholder="Numer ubezpieczenia"
                        required
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700 font-bold mb-2">Ubezpieczyciel</label>
                    <input
                        type="text"
                        value={insurer}
                        onChange={(e) => setInsurer(e.target.value)}
                        className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                        placeholder="Ubezpieczyciel"
                        required
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700 font-bold mb-2">Ostatnie badanie techniczne</label>
                    <input
                        type="date"
                        value={inspectionDate}
                        onChange={(e) => setInspectionDate(e.target.value)}
                        className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                        required
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700 font-bold mb-2">Termin najbliższego badania
                        technicznego</label>
                    <input
                        type="date"
                        value={nextInspectionDate}
                        onChange={(e) => setNextInspectionDate(e.target.value)}
                        className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                        required
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700 font-bold mb-2">Nr karty paliwowej</label>
                    <input
                        type="text"
                        value={fuelCardNumber}
                        onChange={(e) => setFuelCardNumber(e.target.value)}
                        className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                        placeholder="Nr karty paliwowej"
                        required
                    />
                </div>

                <button
                    type="submit"
                    className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                    Dodaj samochód
                </button>

            </form>
        </>
    )
}

export default NewCar;