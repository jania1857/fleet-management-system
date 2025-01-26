import React from "react";
import {Vehicle, vehicles} from "../../../mocks/cars.ts";
import image from "../../../assets/placeholder.jpg";
import {useParams} from "react-router-dom";
import Accordion from "../../../components/Accordion.tsx";
import RefuelingTable from "../../../components/RefuelingTable.tsx";
import {refuelings} from "../../../mocks/refuelings.ts";
import FuelConsumptionChart from "../../../components/FuelConsumptionChart.tsx";

const CarDetails: React.FC = () => {

    const {id} = useParams<{ id: string }>();
    const vehicle: Vehicle = vehicles.find(v => v.id.toString() === id) as Vehicle;

    if (!vehicle) {
        return (
            <h1 className="text-5xl text-red-500">[404] Brak pojazdu o podanym ID</h1>
        )
    }

    return (
        <>
            <h1 className="text-5xl text-left font-bold">{vehicle.make} {vehicle.model} {vehicle.year}</h1>
            <hr className="border-gray-800"/>
            <div className="flex justify-between">
                <h3 className="text-xl text-left">ID: {vehicle.id} | VIN: {vehicle.vin}</h3>
                <h3 className="text-xl text-left">Aktualny przebieg: {vehicle.mileage} km</h3>
            </div>
            <div className="flex text-left gap-20">
                <img
                    src={image}
                    alt=""
                    className="w-full max-w-sm rounded-lg border border-gray-200 shadow-md"
                />
                <table>
                    <tbody>
                    <tr>
                        <td className="font-bold">Marka:</td>
                        <td>{vehicle.make}</td>
                    </tr>
                    <tr>
                        <td className="font-bold">Model:</td>
                        <td>{vehicle.model}</td>
                    </tr>
                    <tr>
                        <td className="font-bold">Rok produkcji:</td>
                        <td>{vehicle.year}</td>
                    </tr>
                    <tr>
                        <td className="font-bold">Nr rejestracyjny:</td>
                        <td>{vehicle.registrationNumber}</td>
                    </tr>
                    <tr>
                        <td className="font-bold">VIN:</td>
                        <td>{vehicle.vin}</td>
                    </tr>
                    <tr>
                        <td className="font-bold">Status:</td>
                        <td>{vehicle.status}</td>
                    </tr>
                    <tr>
                        <td className="font-bold">Rodzaj napędu:</td>
                        <td>{vehicle.fuel}</td>
                    </tr>
                    <tr>
                        <td className="font-bold">Pojemność silnika:&nbsp;</td>
                        <td>{vehicle.displacement}</td>
                    </tr>
                    <tr>
                        <td className="font-bold">Przebieg:</td>
                        <td>{vehicle.mileage} km</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <hr className="mt-5 mb-5 border-gray-800"/>
            <Accordion title="Historia przebiegu">
                <RefuelingTable isAdmin={true} refuelings={refuelings} />
            </Accordion>
            <Accordion title="Wykres paliwa">
                <FuelConsumptionChart />
            </Accordion>
        </>
    )
}

export default CarDetails;