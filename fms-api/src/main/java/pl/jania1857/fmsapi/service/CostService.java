package pl.jania1857.fmsapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.jania1857.fmsapi.repository.CostRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CostService {
    private final CostRepository costRepository;
}
