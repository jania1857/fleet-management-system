import React, {useEffect, useState} from "react";
import image from "../../../assets/placeholder.jpg";
import {useParams} from "react-router-dom";
import {VehicleDto} from "../../../generated-client";
import {allApi} from "../../../api/apiClient.ts";
import {getVehicleMileage, getVehicleStatus} from "../../../util/utils.ts";

const CarDetails: React.FC = () => {

    const [vehicle, setVehicle] = useState<VehicleDto>();
    const [loading, setLoading] = useState(true);

    const {id} = useParams<{ id: string }>();
    if (!id) {
        return (
            <h1 className="text-5xl text-red-500">Nie przekazano ID pojazdu</h1>
        )
    }
    const numberId: number = +id;

    useEffect(() => {
        allApi.getVehicle(numberId || 1)
            .then(response => {
                setVehicle(response.data);
            })
            .catch((error) => {
                return (
                    <h1 className="text-5xl text-red-500">[404] Brak pojazdu o podanym ID - ${error}</h1>
                )
            })
            .finally(() => setLoading(false));
    }, []);

    if (loading) {
        return (
            <p>Ładowanie</p>
        )
    }
    if(!vehicle) {
        return (
            <h1 className="text-5xl text-red-500">[404] Brak pojazdu o podanym ID - ${id}</h1>
        )

    }

    return (
        <>
            <h1 className="text-5xl text-left font-bold">{vehicle.manufacturer} {vehicle.model} {vehicle.year}</h1>
            <hr className="border-gray-800"/>
            <div className="flex justify-between">
                <h3 className="text-xl text-left">ID: {vehicle.id} | VIN: {vehicle.vin}</h3>
                <h3 className="text-xl text-left">Aktualny przebieg: {getVehicleMileage(vehicle)?.newMileage} km</h3>
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
                        <td>{vehicle.manufacturer}</td>
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
                        <td>{getVehicleStatus(vehicle)?.newStatus}</td>
                    </tr>
                    <tr>
                        <td className="font-bold">Rodzaj napędu:</td>
                        <td>{vehicle.fuelType}</td>
                    </tr>
                    <tr>
                        <td className="font-bold">Pojemność silnika:&nbsp;</td>
                        <td>{vehicle.displacement}</td>
                    </tr>
                    <tr>
                        <td className="font-bold">Przebieg:</td>
                        <td>{getVehicleMileage(vehicle)?.newMileage} km</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <hr className="mt-5 mb-5 border-gray-800"/>
        </>
    )
}

export default CarDetails;