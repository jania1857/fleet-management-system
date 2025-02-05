package pl.jania1857.fmsapi.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.jania1857.fmsapi.repository.VehicleRepository;
import pl.jania1857.fmsapi.service.VehicleService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Service
public class Scheduler {

    private final RestTemplate restTemplate;
    private final VehicleService vehicleService;
    private final VehicleRepository vehicleRepository;

    private LocalDateTime lastFetchShell;


    // 2-hour interval
    @Scheduled(fixedRate = 7200000)
    public void fetchRefuelingsShell() {
        LocalDateTime lastFetch = LocalDateTime.now().minusHours(2);
        LocalDateTime now = LocalDateTime.now();
        if (lastFetchShell != null) {
            lastFetch = lastFetchShell;
        }

        String currentDateTimeISO = now.format(DateTimeFormatter.ISO_DATE_TIME);
        String lastFetchDateTimeISO = lastFetch.format(DateTimeFormatter.ISO_DATE_TIME);
    }

}
