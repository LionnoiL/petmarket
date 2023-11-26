package org.petmarket.location.service;

import jakarta.transaction.Transactional;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.location.dto.CityRequestDto;
import org.petmarket.location.dto.CityResponseDto;
import org.petmarket.location.entity.City;
import org.petmarket.location.entity.District;
import org.petmarket.location.entity.State;
import org.petmarket.location.mapper.CityMapper;
import org.petmarket.location.repository.CityRepository;
import org.petmarket.location.repository.DistrictRepository;
import org.petmarket.location.repository.StateRepository;
import org.petmarket.utils.ErrorUtils;
import org.petmarket.utils.TransliterateUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

import static org.petmarket.utils.MessageUtils.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class CityService {

    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final DistrictRepository districtRepository;
    private final CityMapper cityMapper;
    private final TransliterateUtils transliterateUtils;

    public CityResponseDto findById(Long id) {
        return cityMapper.mapEntityToDto(getCity(id));
    }

    public CityResponseDto findByKoatuu(String koatuu) {
        City city = cityRepository.findByKoatuuCode(koatuu).orElseThrow(() -> {
            throw new ItemNotFoundException(CITY_NOT_FOUND);
        });
        return cityMapper.mapEntityToDto(city);
    }

    public List<CityResponseDto> findByName(String name, int size) {
        return cityRepository.findByNameContainingOrderByName(name, PageRequest.of(0, size))
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

    public List<City> getByIds(List<Long> citiesIds) {
        if (citiesIds == null) {
            return Collections.emptyList();
        }
        return cityRepository.findAllById(citiesIds);
    }

    @Transactional
    public void deleteCity(Long id) {
        City city = getCity(id);
        cityRepository.delete(city);
    }

    @Transactional
    public CityResponseDto addCity(CityRequestDto request, BindingResult bindingResult) {
        ErrorUtils.checkItemNotCreatedException(bindingResult);

        State state = getState(request.getStateId());
        City city = City.builder()
                .state(state)
                .name(request.getName())
                .alias(transliterateUtils.getAlias(City.class.getSimpleName(), request.getName()))
                .build();
        city.setKoatuuCode(request.getKoatuuCode());
        city.setCityTypeName(request.getCityTypeName());
        city.setCityTypeShortName(request.getCityTypeShortName());
        fillDistrict(request, city);
        cityRepository.save(city);

        return cityMapper.mapEntityToDto(city);
    }

    @Transactional
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
        city.setKoatuuCode(request.getKoatuuCode());
        city.setCityTypeName(request.getCityTypeName());
        city.setCityTypeShortName(request.getCityTypeShortName());

        fillDistrict(request, city);
        cityRepository.save(city);

        return cityMapper.mapEntityToDto(city);
    }

    private void fillDistrict(CityRequestDto request, City city) {
        if (request.getDistrictId() != null) {
            District district = districtRepository.findById(request.getDistrictId()).orElseThrow(
                    () -> {
                        throw new ItemNotFoundException(DISTRICT_NOT_FOUND);
                    }
            );
            city.setDistrict(district);
        }
    }

    private City getCity(Long id) {
        return cityRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException(CITY_NOT_FOUND);
        });
    }

    private State getState(Long stateId) {
        return stateRepository.findById(stateId).orElseThrow(() -> {
            throw new ItemNotFoundException(STATE_NOT_FOUND);
        });
    }
}
