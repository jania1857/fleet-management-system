package pl.jania1857.fmsapi.scheduler;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.jania1857.fmsapi.dto.shell.Transaction;
import pl.jania1857.fmsapi.model.Vehicle;
import pl.jania1857.fmsapi.repository.VehicleRepository;
import pl.jania1857.fmsapi.service.VehicleService;
import pl.jania1857.fmsapi.utils.Fuel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class Scheduler {

    private final VehicleService vehicleService;
    private final VehicleRepository vehicleRepository;
    private final WebClient shellApiClient;


    private LocalDateTime lastFetchShell;

    public Scheduler(
            VehicleService vehicleService,
            VehicleRepository vehicleRepository,
            WebClient.Builder webClientBuilder
    ) {
        this.vehicleService = vehicleService;
        this.vehicleRepository = vehicleRepository;
        this.shellApiClient = webClientBuilder.baseUrl("http://localhost:8081/api").build();
    }


    //2-hour interval
    @Scheduled(fixedRate = 5000)
    public void fetchRefuelingsShell() {
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

}
