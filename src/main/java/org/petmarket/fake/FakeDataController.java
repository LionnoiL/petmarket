package org.petmarket.fake;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.utils.annotations.responses.ApiResponseForbidden;
import org.petmarket.utils.annotations.responses.ApiResponseSuccessful;
import org.petmarket.utils.annotations.responses.ApiResponseUnauthorized;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Fake data", description = "API for creating test data")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/admin/fake")
public class FakeDataController {

    private final FakeDataService fakeDataService;

    @Operation(summary = "Send request to create users")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PostMapping("/users")
    public ResponseEntity<Void> createUsers(@RequestParam int count) {
        fakeDataService.createUsers(count);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Send request to create advertisements")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PostMapping("/advertisements")
    public ResponseEntity<Void> createAdvertisements(@RequestParam int count) {
        fakeDataService.createAdvertisements(count);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
