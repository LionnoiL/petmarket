package org.petmarket.users.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Data;
import org.petmarket.users.entity.Role;
import org.petmarket.users.entity.UserStatus;
import org.petmarket.users.entity.UserType;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class UserResponseDto {

    private long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date created;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date updated;

    private UserStatus status;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String patronymic;
    private String email;
    private String mainPhone;
    private Set<UserPhoneDto> phones;
    private String site;
    private int rating;

    @JsonProperty("reviews_count")
    private int reviewsCount;

    @JsonProperty("user_avatar_url")
    private String userAvatarUrl;

    @JsonProperty("facebook_link")
    private String facebookLink;

    @JsonProperty("instagram_link")
    private String instagramLink;

    @JsonProperty("twitter_link")
    private String twitterLink;

    @Schema(example = "2001-09-01", description = "User birthday")
    private LocalDate birthday;

    private int birthdayDay;
    private int birthdayMonth;
    private int birthdayYear;

    //TODO location, language

    private List<Role> roles;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("last_activity")
    private LocalDateTime lastActivity;

    @JsonProperty("user_type")
    private UserType userType;
}
