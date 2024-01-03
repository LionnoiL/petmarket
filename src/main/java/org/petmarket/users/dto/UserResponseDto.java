package org.petmarket.users.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.petmarket.users.entity.Role;
import org.petmarket.users.entity.UserStatus;
import org.petmarket.users.entity.UserType;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    private String email;
    private String phone;
    private String site;
    private int rating;

    @JsonProperty("reviews_count")
    private int reviewsCount;

    @JsonProperty("user_avatar_url")
    private String userAvatarUrl;

    //TODO location, language

    private List<Role> roles;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("last_activity")
    private LocalDateTime lastActivity;

    @JsonProperty("user_type")
    private UserType userType;
}
