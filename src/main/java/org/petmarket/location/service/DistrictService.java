package org.petmarket.location.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.location.dto.DistrictRequestDto;
import org.petmarket.location.dto.DistrictResponseDto;
import org.petmarket.location.entity.District;
import org.petmarket.location.entity.State;
import org.petmarket.location.mapper.DistrictMapper;
import org.petmarket.location.repository.DistrictRepository;
import org.petmarket.location.repository.StateRepository;
import org.petmarket.utils.ErrorUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import static org.petmarket.utils.MessageUtils.DISTRICT_NOT_FOUND;
import static org.petmarket.utils.MessageUtils.STATE_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class DistrictService {

    private final DistrictRepository districtRepository;
    private final StateRepository stateRepository;
    private final DistrictMapper districtMapper;

    public DistrictResponseDto findById(Long id) {
        return districtMapper.mapEntityToDto(getDistrict(id));
    }

    public DistrictResponseDto findByKoatuu(String koatuu) {
        District district = districtRepository.findByKoatuuCode(koatuu).orElseThrow(() -> {
            throw new ItemNotFoundException(DISTRICT_NOT_FOUND);
        });
        return districtMapper.mapEntityToDto(district);
    }

    @Transactional
    public DistrictResponseDto addDistrict(DistrictRequestDto request, BindingResult bindingResult) {
        ErrorUtils.checkItemNotCreatedException(bindingResult);

        State state = getState(request.getStateId());
        District district = District.builder()
                .state(state)
                .name(request.getName())
                .koatuuCode(request.getKoatuuCode())
                .build();
        districtRepository.save(district);

        return districtMapper.mapEntityToDto(district);
    }

    @Transactional
    public DistrictResponseDto updateDistrict(Long id, DistrictRequestDto request,
                                              BindingResult bindingResult) {
        ErrorUtils.checkItemNotUpdatedException(bindingResult);

        District district = getDistrict(id);
        State state = getState(request.getStateId());

        district.setName(request.getName());
        district.setState(state);
        district.setKoatuuCode(request.getKoatuuCode());
        districtRepository.save(district);

        return districtMapper.mapEntityToDto(district);
    }

    @Transactional
    public void deleteDistrict(Long id) {
        District district = getDistrict(id);
        districtRepository.delete(district);
    }

    private District getDistrict(Long id) {
        return districtRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException(DISTRICT_NOT_FOUND);
        });
    }

    private State getState(Long stateId) {
        return stateRepository.findById(stateId).orElseThrow(() -> {
            throw new ItemNotFoundException(STATE_NOT_FOUND);
        });
    }
}
