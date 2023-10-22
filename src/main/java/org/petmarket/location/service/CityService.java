package org.petmarket.location.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.errorhandling.ItemNotCreatedException;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.errorhandling.ItemNotUpdatedException;
import org.petmarket.location.dto.CityRequestDto;
import org.petmarket.location.dto.CityResponseDto;
import org.petmarket.location.entity.City;
import org.petmarket.location.entity.State;
import org.petmarket.location.mapper.CityMapper;
import org.petmarket.location.repository.CityRepository;
import org.petmarket.location.repository.StateRepository;
import org.petmarket.utils.ErrorUtils;
import org.petmarket.utils.TransliterateUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Slf4j
@RequiredArgsConstructor
@Service
public class CityService {

    public static final String CITY_NOT_FOUND_MESSAGE = "City not found";
    public static final String STATE_NOT_FOUND_MESSAGE = "State not found";
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final CityMapper cityMapper;
    private final TransliterateUtils transliterateUtils;

    public CityResponseDto findById(Long id) {
        return cityRepository.findById(id)
            .map(cityMapper::mapEntityToDto)
            .orElseThrow(() -> new ItemNotFoundException(CITY_NOT_FOUND_MESSAGE));
    }

    public List<CityResponseDto> findByName(String name) {
        return cityRepository.findByNameContainingOrderByName(name)
            .stream()
            .map(cityMapper::mapEntityToDto)
            .toList();
    }

    public void deleteCity(Long id) {
        City city = cityRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException(CITY_NOT_FOUND_MESSAGE);
        });
        cityRepository.delete(city);
    }

    public CityResponseDto addCity(CityRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ItemNotCreatedException(ErrorUtils.getErrorsString(bindingResult));
        }
        State state = stateRepository.findById(request.getStateId()).orElseThrow(() -> {
            throw new ItemNotFoundException(STATE_NOT_FOUND_MESSAGE);
        });
        City city = City.builder()
            .state(state)
            .name(request.getName())
            .alias(transliterateUtils.getAlias(City.class.getSimpleName(), request.getName()))
            .build();
        cityRepository.save(city);

        return cityMapper.mapEntityToDto(city);
    }

    public CityResponseDto updateCity(Long id, CityRequestDto request,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ItemNotUpdatedException(ErrorUtils.getErrorsString(bindingResult));
        }
        City city = cityRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException(CITY_NOT_FOUND_MESSAGE);
        });
        State state = stateRepository.findById(request.getStateId()).orElseThrow(() -> {
            throw new ItemNotFoundException(STATE_NOT_FOUND_MESSAGE);
        });

        if (!city.getName().equals(request.getName())) {
            city.setAlias(
                transliterateUtils.getAlias(City.class.getSimpleName(), request.getName())
            );
        }
        city.setName(request.getName());
        city.setState(state);
        cityRepository.save(city);

        return cityMapper.mapEntityToDto(city);
    }
}
