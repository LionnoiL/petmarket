package org.petmarket.users.mapper;

import org.mapstruct.Mapper;
import org.petmarket.config.MapperConfig;
import org.petmarket.users.dto.ComplaintRequestDto;
import org.petmarket.users.entity.Complaint;

@Mapper(config = MapperConfig.class)
public interface ComplaintMapper {
    Complaint mapDtoToComplaint(ComplaintRequestDto complaintRequestDto);
}
