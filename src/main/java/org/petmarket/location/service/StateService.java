package org.petmarket.location.service;

import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.location.dto.CityResponseDto;
import org.petmarket.location.dto.StateResponseDto;
import org.petmarket.location.entity.City;
import org.petmarket.location.entity.Country;
import org.petmarket.location.entity.State;
import org.petmarket.location.mapper.CityMapper;
import org.petmarket.location.mapper.StateMapper;
import org.petmarket.location.repository.CountryRepository;
import org.petmarket.location.repository.StateRepository;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class StateService {

    public static final String STATE_NOT_FOUND_MESSAGE = "State not found";
    public static final String COUNTRY_NOT_FOUND_MESSAGE = "Country not found";
    private final StateRepository stateRepository;
    private final CountryRepository countryRepository;
    private final StateMapper stateMapper;
    private final CityMapper cityMapper;

    public StateResponseDto findById(Long id) {
        return stateRepository.findById(id)
            .map(stateMapper::mapEntityToDto)
            .orElseThrow(() -> new ItemNotFoundException(STATE_NOT_FOUND_MESSAGE));
    }

    public List<StateResponseDto> findByName(String name) {
        return stateRepository.findByNameContainingOrderByName(name)
            .stream()
            .map(stateMapper::mapEntityToDto)
            .toList();
    }

    public List<CityResponseDto> getCitiesByStateId(Long id) {
        State state = stateRepository.findById(id)
            .orElseThrow(() -> new ItemNotFoundException(STATE_NOT_FOUND_MESSAGE));
        return state.getCities().stream()
            .sorted(Comparator.comparing(City::getName))
            .map(cityMapper::mapEntityToDto).toList();
    }

    public List<StateResponseDto> findByNameAndCountryId(String name, Long id) {
        Country country = countryRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException(COUNTRY_NOT_FOUND_MESSAGE);
        });
        return stateRepository.findByCountryAndNameContainingOrderByName(country, name)
            .stream()
            .map(stateMapper::mapEntityToDto)
            .toList();
    }
}
