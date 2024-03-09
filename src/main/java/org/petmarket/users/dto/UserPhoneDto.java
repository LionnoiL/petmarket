package org.petmarket.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserPhoneDto {

    private Long id;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("is_main")
    private Boolean main;
}
