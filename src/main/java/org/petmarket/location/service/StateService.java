package org.petmarket.location.service;

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

import java.util.Comparator;
import java.util.List;

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
        return stateMapper.mapEntityToDto(getState(id));
    }

    public StateResponseDto findByKoatuu(String koatuu) {
        State state = stateRepository.findByKoatuuCode(koatuu).orElseThrow(() -> {
            throw new ItemNotFoundException(STATE_NOT_FOUND_MESSAGE);
        });
        return stateMapper.mapEntityToDto(state);
    }

    public List<StateResponseDto> findByName(String name) {
        return stateRepository.findByNameContainingOrderByName(name)
                .stream()
                .map(stateMapper::mapEntityToDto)
                .toList();
    }

    public List<CityResponseDto> getCitiesByStateId(Long id) {
        State state = getState(id);
        return state.getCities().stream()
                .sorted(Comparator.comparing(City::getName))
                .map(cityMapper::mapEntityToDto).toList();
    }

    public List<StateResponseDto> findByNameAndCountryId(String name, Long id) {
        Country country = getCountry(id);
        return stateRepository.findByCountryAndNameContainingOrderByName(country, name)
                .stream()
                .map(stateMapper::mapEntityToDto)
                .toList();
    }

    public List<StateResponseDto> findAll() {
        return stateRepository.findAll()
                .stream()
                .map(stateMapper::mapEntityToDto)
                .toList();
    }

    private State getState(Long stateId) {
        return stateRepository.findById(stateId).orElseThrow(() -> {
            throw new ItemNotFoundException(STATE_NOT_FOUND_MESSAGE);
        });
    }

    private Country getCountry(Long id) {
        return countryRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(COUNTRY_NOT_FOUND_MESSAGE));
    }
}
