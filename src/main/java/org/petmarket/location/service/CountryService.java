package org.petmarket.location.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.location.dto.CountryResponseDto;
import org.petmarket.location.dto.StateResponseDto;
import org.petmarket.location.entity.Country;
import org.petmarket.location.entity.State;
import org.petmarket.location.mapper.CountryMapper;
import org.petmarket.location.mapper.StateMapper;
import org.petmarket.location.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

import static org.petmarket.utils.MessageUtils.COUNTRY_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class CountryService {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;
    private final StateMapper stateMapper;

    public CountryResponseDto findById(Long id) {
        return countryMapper.mapEntityToDto(getCountry(id));
    }

    public List<CountryResponseDto> findByName(String name) {
        return countryRepository.findByNameContainingOrderByName(name)
                .stream()
                .map(countryMapper::mapEntityToDto)
                .toList();
    }

    public List<StateResponseDto> getStatesByCountryId(Long id) {
        Country country = getCountry(id);
        return country.getStates().stream()
                .sorted(Comparator.comparing(State::getName))
                .map(stateMapper::mapEntityToDto)
                .toList();
    }

    private Country getCountry(Long id) {
        return countryRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(COUNTRY_NOT_FOUND));
    }
}
