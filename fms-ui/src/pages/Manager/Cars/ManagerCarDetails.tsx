import React, {useEffect, useState} from "react";
import image from "../../../assets/placeholder.jpg";
import {useParams} from "react-router-dom";
import {ChangeStatusRequest, StatusChangeDtoNewStatusEnum, UserDto, VehicleDto} from "../../../generated-client";
import {allApi, managerApi} from "../../../api/apiClient.ts";
import {getVehicleMileage, getVehicleStatus} from "../../../util/utils.ts";
import Accordion from "../../../components/Accordion.tsx";
import InsuranceForm from "./Forms/InsuranceForm.tsx";
import InspectionForm from "./Forms/InspectionForm.tsx";
import ServiceForm from "./Forms/ServiceForm.tsx";

const ManagerCarDetails: React.FC = () => {

    const [vehicle, setVehicle] = useState<VehicleDto>();
    const [loading, setLoading] = useState(true);
    const [assignmentSelectVisible, setAssignmentSelectVisible] = useState(false);
    const [assignable, setAssignable] = useState<boolean>(true);
    const [drivers, setDrivers] = useState<UserDto[]>([]);
    const [selectedDriverId, setSelectedDriverId] = useState<number | null>(null);
    const [activeForm, setActiveForm] = useState<number>(-1);


    const {id} = useParams<{ id: string }>();

    if (!id) {
        return (
            <h1 className="text-5xl text-red-500">Nie przekazano ID pojazdu</h1>
        )
    }
    const vehicleId: number = +id;

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

        managerApi.getDrivers()
            .then(response => {
                setDrivers(response.data);
            })
            .catch((error) => {
                console.error("Błąd podczas pobierania listy kierowców: ", error);
            })
    }, []);
    useEffect(() => {
        if (vehicle) {
            const vehicleAssignments = vehicle.assignments || [];
            const isAssignable = vehicleAssignments.every((assignment) => assignment.endTime !== null);
            setAssignable(isAssignable);
            setNewMileage(getVehicleMileage(vehicle)?.newMileage)
            setNewStatus(getStatus(vehicle))
        }
    }, [vehicle]);

    const handleAssignment = async () => {
        if (!selectedDriverId || selectedDriverId === 0) {
            alert("Najpierw wybierz kierowcę")
            return;
        }

        await managerApi.newAssignment(selectedDriverId, vehicleId)
            .then(async (response) => {
                if (response.status === 200) {
                    const request: ChangeStatusRequest = {
                        newStatus: "ASSIGNED"
                    }
                    await managerApi.changeStatusForVehicle(vehicleId, request);
                    alert("Pojazd został przypisany do kierowcy");
                    window.location.reload();
                }
            })
            .catch((error) => {
                alert("Błąd podczas przypisywania pojazdu: " + error);
            });
        window.location.reload();
    }
    const handleEndAssignment = async (assignmentId: number) => {
        if (!assignmentId || assignmentId === 0) {
            alert("Błąd podczas zakończenia przypisania: Brak ID przypisania");
            return;
        }

        const confirm = window.confirm(`Czy na pewno chcesz zakończyć przypisanie tego pojazdu?`);
        if (!confirm) return;

        await managerApi.endAssignment(assignmentId)
            .then(async (response) => {
                if (response.status === 200) {
                    const request: ChangeStatusRequest = {
                        newStatus: "READY"
                    }
                    await managerApi.changeStatusForVehicle(vehicleId, request);

                    alert("Pomyślnie zakończono przypisanie");
                    window.location.reload();
                }
            })
            .catch((error) => {
                alert("Błąd podczas zakończenia przypisania: " + error);
            });
        window.location.reload();
    }

    const handleForm = (formId: number) => {
        if (activeForm === formId) {
            setActiveForm(-1);
        } else {
            setActiveForm(formId);
        }
    }


    const getStatus = (vehicle: VehicleDto | undefined): StatusChangeDtoNewStatusEnum => {
        let status: StatusChangeDtoNewStatusEnum | undefined = getVehicleStatus(vehicle)?.newStatus;
        if (!status || !vehicle) {
            status = StatusChangeDtoNewStatusEnum.Ready;
        }
        return status;
    }

    const fetchConfig = async () => {
        await managerApi.getIotConfig(vehicleId)
            .then(async (response) => {
                let finalString = "";

                if (typeof response.data === "string") {
                    finalString = response.data;
                } else {
                    response.data.forEach(c => {
                        finalString += c
                    });
                }

                const blob = new Blob([finalString], {type: "text/plain;charset=utf-8"});

                const today = new Date().toISOString().split("T")[0]
                const url = window.URL.createObjectURL(blob);
                const link = document.createElement("a");
                link.href = url;
                link.setAttribute('download', `config-${vehicleId}-${today}.ini`);
                document.body.appendChild(link);
                link.click();

                window.URL.revokeObjectURL(url);
                document.body.removeChild(link);
            })
            .catch((error) => {
                alert("Wystąpił błąd podczas pobierania konfiguracji dla tego pojazdu!" + error);
            })
    }

    const [newStatus, setNewStatus] = useState<StatusChangeDtoNewStatusEnum>(getStatus(vehicle));
    const [newMileage, setNewMileage] = useState<number | undefined>(undefined);

    const handleSave = async () => {
        if (vehicle === undefined) return;
        await managerApi.updateVehicle(vehicleId, {
            manufacturer: vehicle.manufacturer,
            model: vehicle.model,
            year: vehicle.year,
            registrationNumber: vehicle.registrationNumber,
            vin: vehicle.vin,
            fuelType: vehicle.fuelType,
            displacement: vehicle.displacement,
        })
            .then(async (response) => {
                if (response.status === 200) {
                    await managerApi.changeStatusForVehicle(vehicleId, {
                        newStatus: newStatus,
                    })
                        .then(async (response) => {
                            if (response.status === 200) {
                                await managerApi.newMileage(vehicleId, {
                                    newMileage: newMileage,
                                })
                                    .then(async (response) => {
                                        if (response.status === 200) {
                                            alert("Pomyślnie zaktualizowano dane pojazdu")
                                        }
                                    })
                                    .catch((e) => {
                                        alert("Błąd podczas wprowadzania przebiegu: " + e)
                                    })
                            }
                        })
                        .catch((error) => {
                            alert("Błąd podczas zmieniania statusu: " + error);
                        })

                }
            })
            .catch((error) => {
                alert("Błąd podczas zmieniania informacji o pojeździe: " + error);
            })
        window.location.reload();
    }


    if (loading) {
        return (
            <p>Ładowanie...</p>
        )
    }
    if (!vehicle) {
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
                        <td>
                            <input
                                type='text'
                                className="w-full px-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                                value={vehicle.manufacturer}
                                onChange={(e) => setVehicle({
                                    ...vehicle,
                                    manufacturer: e.target.value
                                })}
                            />
                        </td>
                    </tr>
                    <tr>
                        <td className="font-bold">Model:</td>
                        <td>
                            <input
                                type='text'
                                className="w-full px-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                                value={vehicle.model}
                                onChange={(e) => setVehicle({
                                    ...vehicle,
                                    model: e.target.value
                                })}
                            />
                        </td>
                    </tr>
                    <tr>
                        <td className="font-bold">Rok produkcji:</td>
                        <td>
                            <input
                                type='text'
                                className="w-full px-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                                value={vehicle.year}
                                onChange={(e) => setVehicle({
                                    ...vehicle,
                                    year: Number(e.target.value)
                                })}
                            />
                        </td>
                    </tr>
                    <tr>
                        <td className="font-bold">Nr rejestracyjny:</td>
                        <td>
                            <input
                                type='text'
                                className="w-full px-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                                value={vehicle.registrationNumber}
                                onChange={(e) => setVehicle({
                                    ...vehicle,
                                    registrationNumber: e.target.value
                                })}
                            />
                        </td>
                    </tr>
                    <tr>
                        <td className="font-bold">VIN:</td>
                        <td>
                            <input
                                type='text'
                                className="w-full px-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                                value={vehicle.vin}
                                onChange={(e) => setVehicle({
                                    ...vehicle,
                                    vin: e.target.value
                                })}
                            />
                        </td>
                    </tr>
                    <tr>
                        <td className="font-bold">Status:</td>
                        <td>
                            <select
                                className="w-full px-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                                value={newStatus}
                                onChange={(event) => {
                                    switch (event.target.value) {
                                        case "READY":
                                            setNewStatus(event.target.value);
                                            break;
                                        case "ASSIGNED":
                                            setNewStatus(event.target.value);
                                            break;
                                        case "SERVICE":
                                            setNewStatus(event.target.value);
                                            break;
                                        case "REQUIRES_ATTENTION":
                                            setNewStatus(event.target.value);
                                            break;
                                        case "BAD":
                                            setNewStatus(event.target.value);
                                            break;
                                    }
                                }}
                            >
                                <option value="READY">READY</option>
                                <option value="ASSIGNED">ASSIGNED</option>
                                <option value="SERVICE">SERVICE</option>
                                <option value="REQUIRES_ATTENTION">REQUIRES ATTENTION</option>
                                <option value="BAD">BAD</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td className="font-bold">Rodzaj napędu:</td>
                        <td>
                            <select
                                value={vehicle.fuelType}
                                className="w-full px-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                                onChange={(event) => {
                                    switch (event.target.value) {
                                        case "GASOLINE":
                                            setVehicle({
                                                ...vehicle,
                                                fuelType: event.target.value
                                            })
                                            break;
                                        case "DIESEL":
                                            setVehicle({
                                                ...vehicle,
                                                fuelType: event.target.value
                                            })
                                            break;
                                        case "GASOLINE_HYBRID":
                                            setVehicle({
                                                ...vehicle,
                                                fuelType: event.target.value
                                            })
                                            break;
                                        case "DIESEL_HYBRID":
                                            setVehicle({
                                                ...vehicle,
                                                fuelType: event.target.value
                                            })
                                            break;
                                        case "LPG":
                                            setVehicle({
                                                ...vehicle,
                                                fuelType: event.target.value
                                            })
                                            break;
                                        case "ELECTRIC":
                                            setVehicle({
                                                ...vehicle,
                                                fuelType: event.target.value
                                            })
                                            break;
                                    }
                                }}
                            >
                                <option value="DIESEL">Diesel</option>
                                <option value="GASOLINE">Benzyna</option>
                                <option value="DIESEL_HYBRID">Hybryda (diesel)</option>
                                <option value="GASOLINE_HYBRID">Hybryda (benzyna)</option>
                                <option value="LPG">LPG</option>
                                <option value="ELECTRIC">Elektryczny</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td className="font-bold">Pojemność silnika (cm<sup>3</sup>):&nbsp;</td>
                        <td>
                            <input
                                type='text'
                                className="w-full px-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                                value={vehicle.displacement}
                                onChange={(e) => setVehicle({
                                    ...vehicle,
                                    displacement: Number(e.target.value)
                                })}
                            />
                        </td>
                    </tr>
                    <tr>
                        <td className="font-bold">Przebieg (km):</td>
                        <td>
                            <input
                                type='text'
                                className="w-full px-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                                value={newMileage}
                                onChange={(event) => setNewMileage(Number(event.target.value))}
                            />
                        </td>
                    </tr>
                    </tbody>
                </table>
                <button
                    className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded transition-colors"
                    onClick={() => handleSave()}
                >
                    Zapisz zmiany
                </button>
                <button
                    className={`bg-blue-800 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded transition-colors`}
                    onClick={() => fetchConfig()}
                >
                    Pobierz konfigurację pokładową
                </button>

            </div>
            <hr className="mt-5 mb-5 border-gray-800"/>
            <div className="flex gap-10">
                {
                    assignable && (
                        <button
                            className={`bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded ${assignmentSelectVisible ? "bg-green-800" : ""}`}
                            onClick={() => setAssignmentSelectVisible(!assignmentSelectVisible)}
                        >
                            Nowe przypisanie
                        </button>
                    )
                }

                {
                    assignmentSelectVisible && (
                        <>
                            <select
                                className="px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                                value={selectedDriverId || ""}
                                onChange={(e) => setSelectedDriverId(+e.target.value)}
                            >
                                <option value="">Wybierz kierowcę</option>
                                {
                                    (
                                        drivers.map((driver) => (
                                            <option key={driver.id} value={driver.id}>
                                                [ID:{driver.id}] {driver.firstname} {driver.lastname}
                                            </option>
                                        ))
                                    )
                                }
                            </select>
                            <button
                                className={`bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded`}
                                onClick={() => handleAssignment()}
                            >
                                Przypisz
                            </button>
                        </>
                    )
                }

            </div>
            {
                assignable && (
                    <hr className="mt-5 mb-5 border-gray-800"/>
                )
            }
            <div className="flex gap-10">
                <button
                    className={`${activeForm == 0 ? "bg-blue-700" : "bg-blue-500"} hover:bg-blue-700 text-white font-bold py-2 px-4 rounded`}
                    onClick={() => handleForm(0)}
                >
                    Nowe ubezpieczenie
                </button>
                <button
                    className={`${activeForm == 1 ? "bg-blue-700" : "bg-blue-500"} hover:bg-blue-700 text-white font-bold py-2 px-4 rounded`}
                    onClick={() => handleForm(1)}
                >
                    Nowy przegląd
                </button>
                <button
                    className={`${activeForm == 2 ? "bg-blue-700" : "bg-blue-500"} hover:bg-blue-700 text-white font-bold py-2 px-4 rounded`}
                    onClick={() => handleForm(2)}
                >
                    Nowy serwis
                </button>
            </div>
            {
                (activeForm === 0) && (
                    <InsuranceForm vehicleId={vehicleId} />
                )
            }
            {
                (activeForm === 1) && (
                    <InspectionForm vehicleId={vehicleId} />
                )
            }
            {
                (activeForm === 2) && (
                    <ServiceForm vehicleId={vehicleId} mileage={newMileage} />
                )
            }
            <hr className="mt-5 mb-5 border-gray-800"/>
            <Accordion title="Przypisania pojazdu">
                <table className="min-w-full bg-white border border-gray-200 rounded-lg shadow-md">
                    <thead>
                    <tr>
                        <th
                            className="py-3 px-6 text-right cursor-pointer"
                        >
                            ID
                        </th>
                        <th
                            className="py-3 px-6 text-right cursor-pointer"
                        >
                            Początek
                        </th>
                        <th
                            className="py-3 px-6 text-right cursor-pointer"
                        >
                            Koniec
                        </th>
                        <th
                            className="py-3 px-6 text-right cursor-pointer"
                        >
                            Przypisany kierowca
                        </th>
                        <th
                            className="py-3 px-6 text-right cursor-pointer"
                        >
                            Akcje
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        vehicle.assignments?.map((assignment) => (
                            <tr key={assignment.id}>
                                <td className="py-3 px-6 text-right">{assignment.id}</td>
                                <td className="py-3 px-6 text-right">{assignment.startTime}</td>
                                <td className="py-3 px-6 text-right">{assignment.endTime === "" ? "Aktywne" : assignment.endTime}</td>
                                <td className="py-3 px-6 text-right">
                                    [ID:{assignment.userId}] {drivers.find((driver) => driver.id === assignment.userId)?.firstname} {drivers.find((driver) => driver.id === assignment.userId)?.lastname}
                                </td>
                                <td className="py-3 px-6 text-right">
                                    {

                                        (assignment.endTime === "" || assignment.endTime === null) && (
                                            <button
                                                className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
                                                onClick={() => handleEndAssignment(assignment.id || 0)}
                                            >
                                                Zakończ
                                            </button>
                                        )
                                    }
                                </td>
                            </tr>
                        ))
                    }
                    </tbody>
                </table>
            </Accordion>
            <Accordion title="Ubezpieczenia">
                <table className="min-w-full bg-white border border-gray-200 rounded-lg shadow-md">
                    <thead>
                    <tr>
                        <th
                            className="py-3 px-6 text-right"
                        >
                            ID
                        </th>
                        <th
                            className="py-3 px-6 text-right"
                        >
                            Ubezpieczyciel
                        </th>
                        <th
                            className="py-3 px-6 text-right"
                        >
                            Początek
                        </th>
                        <th
                            className="py-3 px-6 text-right"
                        >
                            Koniec
                        </th>
                        <th
                            className="py-3 px-6 text-right"
                        >
                            Opis
                        </th>
                        <th
                            className="py-3 px-6 text-right"
                        >
                            Typ
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        vehicle.insurances?.map((insurance) => (
                            <tr key={insurance.id}>
                                <td className="py-3 px-6 text-right">{insurance.id}</td>
                                <td className="py-3 px-6 text-right">{insurance.insurer}</td>
                                <td className="py-3 px-6 text-right">{insurance.startDate}</td>
                                <td className="py-3 px-6 text-right">{insurance.endDate}</td>
                                <td className="py-3 px-6 text-right">{insurance.description}</td>
                                <td className="py-3 px-6 text-right">{insurance.type}</td>
                            </tr>
                        ))
                    }
                    </tbody>
                </table>
            </Accordion>
            <Accordion title="Przeglądy techniczne">
                <table className="min-w-full bg-white border border-gray-200 rounded-lg shadow-md">
                    <thead>
                    <tr>
                        <th
                            className="py-3 px-6 text-right"
                        >
                            ID
                        </th>
                        <th
                            className="py-3 px-6 text-right"
                        >
                            Pomyślne
                        </th>
                        <th
                            className="py-3 px-6 text-right"
                        >
                            Data
                        </th>
                        <th
                            className="py-3 px-6 text-right"
                        >
                            Następny przegląd
                        </th>
                        <th
                            className="py-3 px-6 text-right"
                        >
                            Opis
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        vehicle.inspections?.map((inspection) => (
                            <tr key={inspection.id}>
                                <td className="py-3 px-6 text-right">{inspection.id}</td>
                                <td className="py-3 px-6 text-right">{inspection.passed ? "TAK" : "NIE"}</td>
                                <td className="py-3 px-6 text-right">{inspection.inspectionDate}</td>
                                <td className="py-3 px-6 text-right">{inspection.nextInspectionDate}</td>
                                <td className="py-3 px-6 text-right">{inspection.description}</td>
                            </tr>
                        ))
                    }
                    </tbody>
                </table>
            </Accordion>
            <Accordion title="Historia serwisowa">
                <table className="min-w-full bg-white border border-gray-200 rounded-lg shadow-md">
                    <thead>
                    <tr>
                        <th
                            className="py-3 px-6 text-right"
                        >
                            ID
                        </th>
                        <th
                            className="py-3 px-6 text-right"
                        >
                            Serwis
                        </th>
                        <th
                            className="py-3 px-6 text-right"
                        >
                            Przebieg
                        </th>
                        <th
                            className="py-3 px-6 text-right"
                        >
                            Opis
                        </th>
                        <th
                            className="py-3 px-6 text-right"
                        >
                            Cena
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        vehicle.services?.map((service) => (
                            <tr key={service.id}>
                                <td className="py-3 px-6 text-right">{service.id}</td>
                                <td className="py-3 px-6 text-right">{service.name}</td>
                                <td className="py-3 px-6 text-right">{service.mileageAtTheTime}</td>
                                <td className="py-3 px-6 text-right">{service.description}</td>
                                <td className="py-3 px-6 text-right">{service.cost?.amount}</td>
                            </tr>
                        ))
                    }
                    </tbody>
                </table>
            </Accordion>
            <Accordion title="Historia przebiegu">
                <table className="min-w-full bg-white border border-gray-200 rounded-lg shadow-md">
                    <thead>
                    <tr>
                        <th
                            className="py-3 px-6 text-left"
                        >
                            ID
                        </th>
                        <th
                            className="py-3 px-6 text-right"
                        >
                            Przebieg
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        vehicle.mileageChanges?.map((mileageChange) => (
                            <tr key={mileageChange.id}>
                                <td className="py-3 px-6 text-left">{mileageChange.id}</td>
                                <td className="py-3 px-6 text-right">{mileageChange.newMileage}</td>
                            </tr>
                        ))
                    }
                    </tbody>
                </table>
            </Accordion>
            <Accordion title="Historia zmian statusu">
                <table className="min-w-full bg-white border border-gray-200 rounded-lg shadow-md">
                    <thead>
                    <tr>
                        <th
                            className="py-3 px-6 text-left"
                        >
                            ID
                        </th>
                        <th
                            className="py-3 px-6 text-right"
                        >
                            Data
                        </th>
                        <th
                            className="py-3 px-6 text-right"
                        >
                            Nowy status
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        vehicle.statusChanges?.map((statusChange) => (
                            <tr key={statusChange.id}>
                                <td className="py-3 px-6 text-left">{statusChange.id}</td>
                                <td className="py-3 px-6 text-right">{statusChange.timestamp}</td>
                                <td className="py-3 px-6 text-right">{statusChange.newStatus}</td>
                            </tr>
                        ))
                    }
                    </tbody>
                </table>
            </Accordion>
        </>
    )
}

export default ManagerCarDetails;