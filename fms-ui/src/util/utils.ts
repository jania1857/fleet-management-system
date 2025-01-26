import {MileageChangeDto, StatusChangeDto, VehicleDto} from "../generated-client";

export const getVehicleStatus = (vehicle: VehicleDto) => {
    const statusList: StatusChangeDto[] = vehicle.statusChanges || [];

    if (statusList.length === 0) {
        console.error("Brak statusu dla pojazdu o id: ", vehicle.id);
        return null;
    }
    return statusList.reduce((latest, current) => {
        return new Date(current.timestamp || 0) > new Date(latest.timestamp || 0) ? current : latest
    });
}

export const getVehicleMileage = (vehicle: VehicleDto) => {
    const mileageList: MileageChangeDto[] = vehicle.mileageChanges || [];

    if (mileageList.length === 0) {
        console.error("Brak przebiegów dla pojazdu o id: ", vehicle.id);
        return null;
    }
    return mileageList.reduce((latest, highest) =>
        highest.id > latest.id ? highest : latest
    )
}