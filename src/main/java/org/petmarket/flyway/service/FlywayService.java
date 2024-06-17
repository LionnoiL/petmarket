package org.petmarket.flyway.service;

import lombok.RequiredArgsConstructor;
import org.petmarket.flyway.entity.FlywayHistory;
import org.petmarket.flyway.repository.FlywayRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlywayService {
    private final FlywayRepository flywayRepository;

    public Page<FlywayHistory> getFlywayHistory(int page, int size) {
        return flywayRepository.findAll(PageRequest.of(page - 1, size));
    }
}
