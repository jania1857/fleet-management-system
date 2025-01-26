export interface Vehicle {
    id: number;
    model: string;
    make: string;
    year: number;
    registrationNumber: string;
    vin: string;
    status: "ACTIVE" | "INACTIVE" | "SERVICE";
    fuel: "DIESEL" | "GASOLINE" | "HYBRID" | "ELECTRIC";
    displacement: number;
    mileage: number;
}

export const vehicles: Vehicle[] = [
    {
        id: 1,
        model: "Sprinter",
        make: "Mercedes",
        year: 2018,
        registrationNumber: "WAW 12345",
        vin: "1234567890",
        status: "ACTIVE",
        fuel: "DIESEL",
        displacement: 2000,
        mileage: 100000
    },
    {
        id: 2,
        model: "Crafter",
        make: "Volkswagen",
        year: 2019,
        registrationNumber: "WAW 54321",
        vin: "0987654321",
        status: "INACTIVE",
        fuel: "DIESEL",
        displacement: 2500,
        mileage: 120000
    },
    {
        id: 3,
        model: "Transit",
        make: "Ford",
        year: 2020,
        registrationNumber: "WAW 67890",
        vin: "1357924680",
        status: "SERVICE",
        fuel: "DIESEL",
        displacement: 2200,
        mileage: 80000
    },
    {
        id: 4,
        model: "Ducato",
        make: "Fiat",
        year: 2017,
        registrationNumber: "WAW 09876",
        vin: "2468013579",
        status: "ACTIVE",
        fuel: "DIESEL",
        displacement: 2300,
        mileage: 90000
    },
    {
        id: 5,
        model: "Boxer",
        make: "Peugeot",
        year: 2016,
        registrationNumber: "WAW 54321",
        vin: "9876543210",
        status: "INACTIVE",
        fuel: "DIESEL",
        displacement: 2500,
        mileage: 100000
    },
    {
        id: 6,
        model: "Master",
        make: "Renault",
        year: 2015,
        registrationNumber: "WAW 67890",
        vin: "1234567890",
        status: "SERVICE",
        fuel: "DIESEL",
        displacement: 2300,
        mileage: 110000
    },
    {
        id: 7,
        model: "Sprinter",
        make: "Mercedes",
        year: 2018,
        registrationNumber: "WAW 12345",
        vin: "1234567890",
        status: "ACTIVE",
        fuel: "DIESEL",
        displacement: 2000,
        mileage: 100000
    },
    {
        id: 8,
        model: "Crafter",
        make: "Volkswagen",
        year: 2019,
        registrationNumber: "WAW 54321",
        vin: "0987654321",
        status: "INACTIVE",
        fuel: "DIESEL",
        displacement: 2500,
        mileage: 120000
    },
    {
        id: 9,
        model: "Transit",
        make: "Ford",
        year: 2020,
        registrationNumber: "WAW 67890",
        vin: "1357924680",
        status: "SERVICE",
        fuel: "DIESEL",
        displacement: 2200,
        mileage: 80000
    },
    {
        id: 10,
        model: "Ducato",
        make: "Fiat",
        year: 2017,
        registrationNumber: "WAW 09876",
        vin: "2468013579",
        status: "ACTIVE",
        fuel: "DIESEL",
        displacement: 2300,
        mileage: 90000
    },
    {
        id: 11,
        model: "Boxer",
        make: "Peugeot",
        year: 2016,
        registrationNumber: "WAW 54321",
        vin: "9876543210",
        status: "INACTIVE",
        fuel: "DIESEL",
        displacement: 2500,
        mileage: 100000
    },
    {
        id: 12,
        model: "Master",
        make: "Renault",
        year: 2015,
        registrationNumber: "WAW 67890",
        vin: "1234567890",
        status: "SERVICE",
        fuel: "DIESEL",
        displacement: 2300,
        mileage: 110000
    },
    {
        id: 13,
        model: "Sprinter",
        make: "Mercedes",
        year: 2018,
        registrationNumber: "WAW 12345",
        vin: "1234567890",
        status: "ACTIVE",
        fuel: "DIESEL",
        displacement: 2000,
        mileage: 100000
    },
    {
        id: 14,
        model: "Crafter",
        make: "Volkswagen",
        year: 2019,
        registrationNumber: "WAW 54321",
        vin: "0987654321",
        status: "INACTIVE",
        fuel: "DIESEL",
        displacement: 2500,
        mileage: 120000
    },
    {
        id: 15,
        model: "Transit",
        make: "Ford",
        year: 2020,
        registrationNumber: "WAW 67890",
        vin: "1357924680",
        status: "SERVICE",
        fuel: "DIESEL",
        displacement: 2200,
        mileage: 80000
    },
    {
        id: 16,
        model: "Ducato",
        make: "Fiat",
        year: 2017,
        registrationNumber: "WAW 09876",
        vin: "2468013579",
        status: "ACTIVE",
        fuel: "DIESEL",
        displacement: 2300,
        mileage: 90000
    }
]