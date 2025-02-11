package pl.jania1857.fmsapi.scheduler;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.jania1857.fmsapi.dto.ChangeStatusRequest;
import pl.jania1857.fmsapi.dto.shell.Transaction;
import pl.jania1857.fmsapi.model.Inspection;
import pl.jania1857.fmsapi.model.Insurance;
import pl.jania1857.fmsapi.model.StatusChange;
import pl.jania1857.fmsapi.model.Vehicle;
import pl.jania1857.fmsapi.repository.VehicleRepository;
import pl.jania1857.fmsapi.service.VehicleService;
import pl.jania1857.fmsapi.utils.Fuel;
import pl.jania1857.fmsapi.utils.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class Scheduler {

    private final VehicleService vehicleService;
    private final VehicleRepository vehicleRepository;
    private final WebClient shellApiClient;


    private LocalDateTime lastFetchShell;

    private final boolean shellFetchActive = false;
    private final int shellFetchIntervalMs = 60 * 60 * 1000;

    private final boolean vehiclesStatusCheckActive = true;
    private final int vehiclesStatusCheckInterval = 24 * 60 * 60 * 1000;

    public Scheduler(
            VehicleService vehicleService,
            VehicleRepository vehicleRepository,
            WebClient.Builder webClientBuilder
    ) {
        this.vehicleService = vehicleService;
        this.vehicleRepository = vehicleRepository;
        this.shellApiClient = webClientBuilder.baseUrl("http://localhost:8081/api").build();
    }


    //1-hour interval
    @Scheduled(fixedRate = shellFetchIntervalMs)
    public void fetchRefuelingsShell() {
        if (!shellFetchActive) {
            return;
        }

        LocalDateTime lastFetch = LocalDateTime.now().minusHours(2);
        LocalDateTime now = LocalDateTime.now();
        if (lastFetchShell != null) {
            lastFetch = lastFetchShell;
        }

        String currentDateTimeISO = now.format(DateTimeFormatter.ISO_DATE_TIME);
        String lastFetchDateTimeISO = lastFetch.format(DateTimeFormatter.ISO_DATE_TIME);

        String requestUrl = "/transaction/byDate?startDate=" + lastFetchDateTimeISO + "&endDate=" + currentDateTimeISO;

        System.out.println(requestUrl);

        List<Transaction> transactions = shellApiClient.get()
                .uri(requestUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Transaction>>() {})
                .block();

        if (transactions == null || transactions.isEmpty()) {
            return;
        }
        transactions.forEach(transaction -> {
            Vehicle vehicle = vehicleRepository.findByFuelCardNumber(transaction.cardNumber())
                    .orElse(null);
            if (vehicle == null) {
                return;
            }

            Fuel fuel = switch (transaction.fuelType()) {
                case "Gasoline" -> Fuel.GASOLINE;
                case "Electricity" -> Fuel.ELECTRICITY;
                case "LPG" -> Fuel.LPG;
                default -> Fuel.DIESEL;
            };

            vehicleService.newRefuelingByTimestamp(
                    vehicle.getId(),
                    transaction.transactionDate(),
                    transaction.quantity(),
                    transaction.unitPrice(),
                    fuel
            );
        });

        System.out.println((long) transactions.size());

        lastFetchShell = now;
    }

    @Scheduled(fixedRate = vehiclesStatusCheckInterval)
    public void checkVehiclesStatus() {
        if (!vehiclesStatusCheckActive) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        vehicleRepository.findAll().forEach(vehicle -> {
            boolean insuranceGood = checkInsurances(vehicle);
            boolean inspectionGood = checkInspections(vehicle);

            StatusChange latestStatusChange = vehicle.getStatusChanges().stream()
                    .max(Comparator.comparing(StatusChange::getTimestamp)).orElse(null);

            Status status;
            if (latestStatusChange == null) {
                status = Status.REQUIRES_ATTENTION;
            } else {
                status = latestStatusChange.getNewStatus();
            }

            if (insuranceGood && inspectionGood && (status != Status.READY && status != Status.ASSIGNED)) {
                vehicleService.changeStatusForVehicle(vehicle.getId(), new ChangeStatusRequest(Status.READY));
            }
        });
    }

    private boolean checkInsurances(Vehicle vehicle) {
        LocalDateTime now = LocalDateTime.now();
        List<Insurance> insurances = vehicle.getInsurances();
        if (insurances == null || insurances.isEmpty()) {
            vehicleService.changeStatusForVehicle(vehicle.getId(), new ChangeStatusRequest(Status.BAD));
            return false;
        }
        Insurance latestInsurance = insurances.stream()
                .max(Comparator.comparing(Insurance::getEndDate)).orElse(null);

        LocalDateTime latestInsuranceEndDate = latestInsurance.getEndDate();
        if (latestInsuranceEndDate.isBefore(now)) {
            vehicleService.changeStatusForVehicle(vehicle.getId(),
                    new ChangeStatusRequest(Status.BAD));
            return false;
        } else if (latestInsuranceEndDate.isBefore(now.plusDays(14))) {
            vehicleService.changeStatusForVehicle(vehicle.getId(),
                    new ChangeStatusRequest(Status.REQUIRES_ATTENTION));
            return false;
        }
        return true;
    }
    private boolean checkInspections(Vehicle vehicle) {
        LocalDateTime now = LocalDateTime.now();
        List<Inspection> inspections = vehicle.getInspections();
        if (inspections == null || inspections.isEmpty()) {
            vehicleService.changeStatusForVehicle(vehicle.getId(), new ChangeStatusRequest(Status.BAD));
            return false;
        }
        Inspection latestInspection = inspections.stream()
                .max(Comparator.comparing(Inspection::getInspectionDate)).orElse(null);

        LocalDateTime latestInspectionDeadline = latestInspection.getNextInspectionDate();
        if (latestInspectionDeadline.isBefore(now) || !latestInspection.getPassed()) {
            vehicleService.changeStatusForVehicle(vehicle.getId(), new ChangeStatusRequest(Status.BAD));
            return false;
        }

        if (latestInspectionDeadline.isBefore(now.plusDays(14))) {
            vehicleService.changeStatusForVehicle(vehicle.getId(), new ChangeStatusRequest(Status.REQUIRES_ATTENTION));
            return false;
        }
        return true;
    }
}
