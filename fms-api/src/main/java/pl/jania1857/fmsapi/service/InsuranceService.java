package pl.jania1857.fmsapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.jania1857.fmsapi.repository.InsuranceRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class InsuranceService {
    private final InsuranceRepository insuranceRepository;
}
