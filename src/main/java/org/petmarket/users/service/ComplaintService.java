package org.petmarket.users.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.users.dto.ComplaintRequestDto;
import org.petmarket.users.dto.ComplaintResponseDto;
import org.petmarket.users.entity.Complaint;
import org.petmarket.users.entity.ComplaintStatus;
import org.petmarket.users.mapper.ComplaintMapper;
import org.petmarket.users.repository.ComplaintRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ComplaintService {
    private final ComplaintRepository complaintRepository;
    private final ComplaintMapper complaintMapper;
    private final UserService userService;

    @Transactional
    public void addComplaint(ComplaintRequestDto complaintRequestDto) {
        if (complaintRequestDto.getComplainedUserId().equals(UserService.getCurrentUserId())) {
            log.error("User cannot complain about himself");
            throw new IllegalArgumentException("User cannot complain about himself");
        }

        Complaint complaint = complaintMapper.mapDtoToComplaint(complaintRequestDto);
        complaint.setUser(userService.getCurrentUser());
        complaint.setComplainedUser(userService.findById(complaintRequestDto.getComplainedUserId()));
        complaint.setStatus(ComplaintStatus.PENDING);
        complaintRepository.save(complaint);
    }

    public void deleteComplaint(Long id) {
        complaintRepository.deleteById(id);
    }

    public ComplaintResponseDto getComplaint(Long id) {
        return complaintMapper.mapComplaintToDto(complaintRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Complaint not found")));
    }

    public List<ComplaintResponseDto> getComplaints(ComplaintStatus complaintStatus, int size,
                                                    int page, Sort.Direction direction) {
        return complaintMapper.mapComplaintToDto(complaintRepository.findAllByStatus(complaintStatus,
                PageRequest.of(page - 1, size, Sort.by(direction, "created"))).toList());
    }

    public List<ComplaintResponseDto> getComplaintsByUserId(Long userId, ComplaintStatus status,
                                                 int size, int page, Sort.Direction direction) {
        return complaintMapper.mapComplaintToDto(complaintRepository.findAllByComplainedUserIdAndStatus(userId,
                status, PageRequest.of(page - 1, size, Sort.by(direction, "created"))).toList());
    }

    @Transactional
    public void updateStatusById(Long id, ComplaintStatus status) {
        complaintRepository.updateStatusById(id, status);
    }
}
