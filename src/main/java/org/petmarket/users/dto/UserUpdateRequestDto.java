package org.petmarket.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    @Pattern(regexp = "^(http(s)?://)?[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(:[0-9]+)?(\\/[^\\s]*)?$|^$")
    private String site;

    @Pattern(regexp = "^(https?://)?(www\\.)?facebook.com/.+|^$")
    @JsonProperty("facebook")
    private String facebookLink;

    @Pattern(regexp = "^(https?://)?(www\\.)?instagram.com/.+|^$")
    @JsonProperty("instagram")
    private String instagramLink;

    @Pattern(regexp = "^(https?://)?(www\\.)?twitter.com/.+|^$")
    @JsonProperty("twitter")
    private String twitterLink;

    private String about;

    @Pattern(regexp = "^\\+?\\d+$")
    @JsonProperty("main_phone")
    private String mainPhone;

    @Valid
    private Set<@Pattern(regexp = "^\\+?\\d+$") String> phones = new HashSet<>();
}
