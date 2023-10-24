package org.petmarket.location.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.errorhandling.ItemNotFoundException;
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

import java.util.List;

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

    public List<CityResponseDto> findByNameAndStateId(String name, Long id) {
        State state = getState(id);
        return cityRepository.findByStateAndNameContainingOrderByName(state, name)
                .stream()
                .map(cityMapper::mapEntityToDto)
                .toList();
    }

    public void deleteCity(Long id) {
        City city = getCity(id);
        cityRepository.delete(city);
    }

    public CityResponseDto addCity(CityRequestDto request, BindingResult bindingResult) {
        ErrorUtils.checkItemNotCreatedException(bindingResult);

        State state = getState(request.getStateId());
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
        ErrorUtils.checkItemNotUpdatedException(bindingResult);

        City city = getCity(id);
        State state = getState(request.getStateId());

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

    private City getCity(Long id) {
        return cityRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException(CITY_NOT_FOUND_MESSAGE);
        });
    }

    private State getState(Long stateId) {
        return stateRepository.findById(stateId).orElseThrow(() -> {
            throw new ItemNotFoundException(STATE_NOT_FOUND_MESSAGE);
        });
    }
}
