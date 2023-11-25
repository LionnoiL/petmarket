package org.petmarket.advertisements.advertisement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponseDto {

    private long id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String email;
    private String phone;
    private String site;
    private int rating;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss yyyy-MM-dd")
    @JsonProperty("last_activity")
    private LocalDateTime lastActivity;
}
