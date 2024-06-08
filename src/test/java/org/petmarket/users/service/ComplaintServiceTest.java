package org.petmarket.users.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.petmarket.users.dto.ComplaintRequestDto;
import org.petmarket.users.dto.ComplaintResponseDto;
import org.petmarket.users.entity.Complaint;
import org.petmarket.users.entity.ComplaintStatus;
import org.petmarket.users.entity.User;
import org.petmarket.users.mapper.ComplaintMapper;
import org.petmarket.users.repository.ComplaintRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ComplaintServiceTest {
    @Mock
    private ComplaintRepository complaintRepository;

    @Mock
    private ComplaintMapper complaintMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private ComplaintService complaintService;

    @Test
    public void testAddComplaintSelfComplaintThrowsException() {
        try (MockedStatic<UserService> mockedUserService = mockStatic(UserService.class)) {
            // Arrange
            ComplaintRequestDto dto = new ComplaintRequestDto();
            dto.setComplainedUserId(1L);

            mockedUserService.when(UserService::getCurrentUserId).thenReturn(1L);

            // Act
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> complaintService.addComplaint(dto));

            // Assert
            assertEquals("User cannot complain about himself", exception.getMessage());
        }
    }

    @Test
    public void testAddComplaintSuccess() {
        try (MockedStatic<UserService> mockedUserService = mockStatic(UserService.class)) {
            // Arrange
            ComplaintRequestDto dto = new ComplaintRequestDto();
            dto.setComplainedUserId(2L);

            Complaint complaint = new Complaint();
            mockedUserService.when(UserService::getCurrentUserId).thenReturn(1L);
            when(userService.getCurrentUser()).thenReturn(new User());
            when(complaintMapper.mapDtoToComplaint(dto)).thenReturn(complaint);

            // Act
            complaintService.addComplaint(dto);

            // Assert
            verify(complaintRepository, times(1)).save(complaint);
        }
    }

    @Test
    public void testDeleteComplaintSuccess() {
        // Arrange
        Long complaintId = 1L;

        // Act
        complaintService.deleteComplaint(complaintId);

        // Assert
        verify(complaintRepository, times(1)).deleteById(complaintId);
    }

    @Test
    public void testGetComplaintNotFound() {
        // Arrange
        Long complaintId = 1L;
        when(complaintRepository.findById(complaintId)).thenReturn(Optional.empty());

        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> complaintService.getComplaint(complaintId));

        // Assert
        assertEquals("Complaint not found", exception.getMessage());
    }

    @Test
    public void testGetComplaintSuccess() {
        // Arrange
        Long complaintId = 1L;
        Complaint complaint = new Complaint();
        ComplaintResponseDto complaintResponseDto = new ComplaintResponseDto();

        when(complaintRepository.findById(complaintId)).thenReturn(Optional.of(complaint));
        when(complaintMapper.mapComplaintToDto(complaint)).thenReturn(complaintResponseDto);

        // Act
        ComplaintResponseDto result = complaintService.getComplaint(complaintId);

        // Assert
        assertEquals(complaintResponseDto, result);
    }

    @Test
    public void testGetComplaintsSuccess() {
        // Arrange
        ComplaintStatus status = ComplaintStatus.PENDING;
        int page = 1;
        int size = 10;
        Sort.Direction direction = Sort.Direction.ASC;

        Page<Complaint> complaintPage = new PageImpl<>(Collections.emptyList());
        List<Complaint> complaintList = complaintPage.getContent();
        List<ComplaintResponseDto> complaintResponseDtoList = Collections.emptyList();

        when(complaintRepository.findAllByStatus(status,
                PageRequest.of(0, size, Sort.by(direction, "created")))).thenReturn(complaintPage);
        when(complaintMapper.mapComplaintToDto(complaintList)).thenReturn(complaintResponseDtoList);

        // Act
        List<ComplaintResponseDto> result = complaintService.getComplaints(status, size, page, direction);

        // Assert
        assertEquals(complaintResponseDtoList, result);
    }

    @Test
    public void testGetComplaintsByUserIdSuccess() {
        // Arrange
        Long userId = 1L;
        ComplaintStatus status = ComplaintStatus.PENDING;
        int page = 1;
        int size = 10;
        Sort.Direction direction = Sort.Direction.ASC;

        Page<Complaint> complaintPage = new PageImpl<>(Collections.emptyList());
        List<Complaint> complaintList = complaintPage.getContent();
        List<ComplaintResponseDto> complaintResponseDtoList = Collections.emptyList();

        when(complaintRepository.findAllByComplainedUserIdAndStatus(userId, status,
                PageRequest.of(0, size, Sort.by(direction, "created"))))
                .thenReturn(complaintPage);
        when(complaintMapper.mapComplaintToDto(complaintList)).thenReturn(complaintResponseDtoList);

        // Act
        List<ComplaintResponseDto> result = complaintService
                .getComplaintsByUserId(userId, status, size, page, direction);

        // Assert
        assertEquals(complaintResponseDtoList, result);
    }

    @Test
    public void testUpdateStatusById_Success() {
        // Arrange
        Long complaintId = 1L;
        ComplaintStatus status = ComplaintStatus.RESOLVED;

        // Act
        complaintService.updateStatusById(complaintId, status);

        // Assert
        verify(complaintRepository, times(1)).updateStatusById(complaintId, status);
    }
}
