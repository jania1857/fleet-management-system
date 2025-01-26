import React from "react";
import VehicleTable from "../../../components/VehiclesTable.tsx";
import {vehicles} from "../../../mocks/cars.ts";

const AdminCars: React.FC = () => {
    return (
        <>
            <div className="flex mb-5 justify-between">
                <h1 className="text-left text-3xl">Lista pojazdów</h1>
                <button
                    className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded">
                    Dodaj pojazd
                </button>
            </div>
            <VehicleTable isAdmin={true} vehicles={vehicles}/>
        </>
    )
}

export default AdminCars;