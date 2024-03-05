package org.petmarket.advertisements.advertisement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.petmarket.users.dto.UserPhoneDto;
import org.petmarket.users.entity.UserType;
import org.petmarket.users.mapper.IPhones;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class AuthorResponseDto implements IPhones {

    private long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String email;
    private String mainPhone;
    private Set<UserPhoneDto> phones;
    private String site;
    private int rating;

    @JsonProperty("reviews_count")
    private int reviewsCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("last_activity")
    private LocalDateTime lastActivity;

    @JsonProperty("user_type")
    private UserType userType;

    @JsonProperty("user_avatar_url")
    private String userAvatarUrl;
}
