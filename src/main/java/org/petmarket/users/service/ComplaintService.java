package org.petmarket.users.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.users.dto.ComplaintRequestDto;
import org.petmarket.users.entity.Complaint;
import org.petmarket.users.entity.ComplaintStatus;
import org.petmarket.users.mapper.ComplaintMapper;
import org.petmarket.users.repository.ComplaintRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ComplaintService {
    private final ComplaintRepository complaintRepository;
    private final ComplaintMapper complaintMapper;
    private final UserService userService;

    public void addComplaint(ComplaintRequestDto complaintRequestDto) {
        if (complaintRequestDto.getComplainedUserId().equals(UserService.getCurrentUserId())) {
            log.error("User cannot complain about himself");
            throw new IllegalArgumentException("User cannot complain about himself");
        }

        log.info("Adding complaint: {}", complaintRequestDto);
        Complaint complaint = complaintMapper.mapDtoToComplaint(complaintRequestDto);
        complaint.setUser(userService.getCurrentUser());
        complaintRepository.save(complaint);
    }

    public void deleteComplaint(Long id) {
        log.info("Deleting complaint with id: {}", id);
        complaintRepository.deleteById(id);
    }

    public Complaint getComplaint(Long id) {
        log.info("Getting complaint with id: {}", id);
        return complaintRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Complaint not found"));
    }

    public Page<Complaint> getComplaints(ComplaintStatus complaintStatus, int size,
                                         int page, Sort.Direction direction) {
        log.info("Getting all complaints");
        return complaintRepository.findAllByStatus(complaintStatus,
                PageRequest.of(page - 1, size, Sort.by(direction, "created")));
    }

    public Page<Complaint> getComplaintsByUserId(Long userId, ComplaintStatus status,
                                                 int size, int page, Sort.Direction direction) {
        log.info("Getting all complaints by user id: {}", userId);
        return complaintRepository.findAllByComplainedUserIdAndStatus(userId, status, PageRequest
                .of(page - 1, size, Sort.by(direction, "created")));
    }
}
