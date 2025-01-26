import React from "react";
import { Line } from "react-chartjs-2";
import {
    Chart as ChartJS,
    LineElement,
    PointElement,
    CategoryScale,
    LinearScale,
    Tooltip,
    Legend,
} from "chart.js";
import {refuelings} from "../mocks/refuelings.ts";

ChartJS.register(LineElement, PointElement, CategoryScale, LinearScale, Tooltip, Legend);

const FuelConsumptionChart: React.FC = () => {
    const dates = refuelings.map((r) => r.timestamp);
    const consumptions = refuelings.map((r) => r.totalAmount);

    const data = {
        labels: dates,
        datasets: [
            {
                label: "Średnie spalanie",
                data: consumptions,
                borderColor: "rgba(75, 192, 192, 1)",
                backgroundColor: "rgba(75, 192, 192, 0.2)",
                tension: 0.4,
                fill: true
            },
        ],
    };

    const options = {
        responsive: true,
        plugins: {
            legend: {
                display: true,
                position: "top" as const,
            },
            tooltip: {
                enabled: true,
            },
        },
        scales: {
            x: {
                grid: {
                    color: "rgba(200, 200, 200, 0.2)",
                },
                beginAtZero: true,
            },
        },
    };

    return <Line data={data} options={options} />;
}

export default FuelConsumptionChart;