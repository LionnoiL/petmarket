package org.petmarket.users.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.users.dto.ComplaintRequestDto;
import org.petmarket.users.service.ComplaintService;
import org.petmarket.utils.annotations.responses.ApiResponseBadRequest;
import org.petmarket.utils.annotations.responses.ApiResponseSuccessful;
import org.petmarket.utils.annotations.responses.ApiResponseUnauthorized;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Complaints", description = "the user complaints API")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/complaints")
public class ComplaintsController {
    private final ComplaintService complaintService;

    @Operation(summary = "Add complaint")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseBadRequest
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public void addComplaint(@RequestBody @Valid ComplaintRequestDto complaintRequestDto) {
        log.info("Adding complaint");
        complaintService.addComplaint(complaintRequestDto);
    }
}
