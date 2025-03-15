import React, {useEffect, useState} from "react";
import {NewRefuelingRequestFuelEnum, VehicleDto} from "../../../generated-client";
import {allApi} from "../../../api/apiClient.ts";

interface RefuelingPopUpProps {
    setVisible: (visible: number) => void;
    vehicleId: number;
}

const RefuelingPopUp: React.FC<RefuelingPopUpProps> = ({setVisible, vehicleId}) => {

    const [vehicle, setVehicle] = useState<VehicleDto>();
    const [loading, setLoading] = useState(true);

    const [fuelType, setFuelType] = useState<NewRefuelingRequestFuelEnum>();
    const [fuelPrice, setFuelPrice] = useState(0);
    const [fuelVolume, setFuelVolume] = useState(0);
    const [amount, setAmount] = useState(0);

    const handleRefueling = () => {
        allApi.newRefueling(
            vehicleId,
            {

            }
        )
    }

    useEffect(() => {
        allApi.getVehicle(vehicleId || 1)
            .then(response => {
                setVehicle(response.data);
            })
            .catch((error) => {
                return (
                    <h1 className="text-5xl text-red-500">[404] Brak pojazdu o podanym ID - ${error}</h1>
                )
            })
            .finally(() => setLoading(false));

        if (vehicle != undefined) {
            console.log(vehicle.fuelType)
            switch(vehicle.fuelType) {
                case "DIESEL_HYBRID":
                    console.log("DUPA")
                    setFuelType("DIESEL");
                    console.log(fuelType);
                    break;
                case "DIESEL":
                    setFuelType("DIESEL");
                    break;
                case "GASOLINE_HYBRID":
                    setFuelType("DIESEL");
                    break;
                case "GASOLINE":
                    setFuelType("DIESEL");
                    break;
                case "ELECTRIC":
                    setFuelType("DIESEL");
                    break;
                case "LPG":
                    setFuelType("GASOLINE");
                    break;
            }
        }

        console.log(vehicle);

    }, [vehicleId]);


    if (loading) {
        return "Ładowanie..."
    }

    return (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
            <div className="bg-white p-6 rounded-lg shadow-lg">
                <h2 className="text-xl font-bold">Ręczne wprowadzenie tankowania</h2>
                <p className="mt-2">Tylko dla tankowań wykonanych bez wykorzystania karty paliwowej!</p>
                <div
                    className="flex justify-center items-center gap-2 flex-col"
                >
                    <div>
                        <label className="block text-gray-700 font-bold">Paliwo</label>
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
                                    case "ELECTRICITY":
                                        setFuelType(e.target.value)
                                        break;
                                }
                            }}
                            required
                        >
                            <option value="DIESEL">Diesel</option>
                            <option value="GASOLINE">Benzyna</option>
                            <option value="ELECTRICITY">Prąd</option>
                        </select>
                    </div>
                    <div>
                        <label className="block text-gray-700 font-bold">Cena</label>
                        <input
                            type="number"
                            className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                            value={fuelPrice}
                            min={0}
                            step={0.01}
                            onChange={(e) => {
                                setFuelPrice(Number(e.target.value));
                                setAmount(Math.ceil(Number(e.target.value) * fuelVolume * 100) / 100);
                            }}
                        />
                    </div>
                    <div>
                        <label className="block text-gray-700 font-bold">Ilość</label>
                        <input
                            type="number"
                            className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                            value={fuelVolume}
                            min={0}
                            step={0.01}
                            onChange={(e) => {
                                setFuelVolume(Number(e.target.value));
                                setAmount(Math.ceil(fuelPrice * Number(e.target.value) * 100) / 100);
                            }}
                        />
                    </div>
                    <div>
                        <label className="block text-gray-700 font-bold">Cena</label>
                        <input
                            type="number"
                            className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                            value={amount}
                            min={0}
                        />
                    </div>
                </div>
                <div className="flex items-center justify-between px-6 py-2 text-gray-600">
                    <button
                        onClick={() => setVisible(-1)}
                        className="mt-4 px-4 py-2 bg-red-500 text-white rounded-lg"
                    >
                        Anuluj
                    </button>
                    <button
                        onClick={() => {
                            handleRefueling();
                            setVisible(-1)
                        }}
                        className="mt-4 px-4 py-2 bg-blue-500 text-white rounded-lg"
                    >
                        Wprowadź
                    </button>
                </div>

            </div>
        </div>
    )
}

export default RefuelingPopUp;