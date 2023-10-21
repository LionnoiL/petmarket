package org.petmarket.location.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.location.dto.CityResponseDto;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CityService {

    public CityResponseDto findById(Long id) {
        return null;
    }

    public List<CityResponseDto> findByName(String name) {
        return null;
    }
}
