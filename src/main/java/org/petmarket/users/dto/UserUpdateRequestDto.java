package org.petmarket.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
@JsonPropertyOrder({"first_name", "last_name", "patronymic", "birthday", "email", "site",
    "facebook", "instagram", "twitter", "about", "main_phone", "phones", "image"})
public class UserUpdateRequestDto {

    @Schema(example = "Vasil")
    @JsonProperty("first_name")
    private String firstName;

    @Schema(example = "Fedko")
    @JsonProperty("last_name")
    private String lastName;

    @Schema(example = "Ivanovich")
    private String patronymic;

    @Schema(example = "1942-10-9")
    private LocalDate birthday;

    @NotNull(message = "The 'email' cannot be null")
    @NotBlank(message = "The 'email' cannot be empty")
    @Pattern(regexp = "^([^ ]+@[^ ]+\\.[a-z]{2,6}|)$")
    @Schema(example = "fedko@ukr.net")
    private String email;
    private String site;
    @JsonProperty("facebook")
    private String facebookLink;
    @JsonProperty("instagram")
    private String instagramLink;
    @JsonProperty("twitter")
    private String twitterLink;
    private String about;
    @JsonProperty("main_phone")
    private String mainPhone;
    private Set<String> phones = new HashSet<>();
}
