import {MileageChangeDto, StatusChangeDto, VehicleDto} from "../generated-client";

export const getVehicleStatus = (vehicle: VehicleDto | undefined) => {
    if (!vehicle) {
        return null;
    }

    const statusList: StatusChangeDto[] = vehicle.statusChanges || [];

    if (statusList.length === 0) {
        console.error("Brak statusu dla pojazdu o id: ", vehicle.id);
        return null;
    }
    return statusList.reduce((latest, current) => {
        return new Date(current.timestamp || 0) > new Date(latest.timestamp || 0) ? current : latest
    });
}

export const getVehicleMileage = (vehicle: VehicleDto | undefined) => {
    if (!vehicle) {
        return undefined;
    }
    const mileageList: MileageChangeDto[] = vehicle.mileageChanges || [];

    if (mileageList.length === 0) {
        console.error("Brak przebiegów dla pojazdu o id: ", vehicle.id);
        return null;
    }
    return mileageList.reduce((latest, highest) => {
            // @ts-ignore
            return highest.id > latest.id ? highest : latest
        }
    )
}

export const convertDateToDateTime = (date: Date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");

    const hours = String(date.getHours()).padStart(2, "0");
    const minutes = String(date.getMinutes()).padStart(2, "0");
    const seconds = String(date.getSeconds()).padStart(2, "0");

    return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
}

export const isVehicleAssignable = (vehicle: VehicleDto) => {
    const status = getVehicleStatus(vehicle);
    return status?.newStatus === "READY" || status?.newStatus === "REQUIRES_ATTENTION";
}