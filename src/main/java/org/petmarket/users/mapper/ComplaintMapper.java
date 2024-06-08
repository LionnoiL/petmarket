package org.petmarket.users.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petmarket.config.MapperConfig;
import org.petmarket.users.dto.ComplaintRequestDto;
import org.petmarket.users.dto.ComplaintResponseDto;
import org.petmarket.users.entity.Complaint;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface ComplaintMapper {
    Complaint mapDtoToComplaint(ComplaintRequestDto complaintRequestDto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "complainedUserId", source = "complainedUser.id")
    ComplaintResponseDto mapComplaintToDto(Complaint complaint);

    List<ComplaintResponseDto> mapComplaintToDto(List<Complaint> complaints);
}
