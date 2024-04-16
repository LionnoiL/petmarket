package org.petmarket.advertisements.advertisement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.petmarket.users.entity.UserType;

import java.time.LocalDateTime;

@Data
public class AuthorResponseDto {

    private long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String email;
    private int rating;

    @JsonProperty("reviews_count")
    private int reviewsCount;

    @Schema(example = "5", description = "Speed of message response in minutes")
    @JsonProperty("response_speed")
    private int responseSpeed;

    @JsonProperty("complete_orders_count")
    private int completeOrdersCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("last_activity")
    private LocalDateTime lastActivity;

    @JsonProperty("user_type")
    private UserType userType;

    @JsonProperty("user_avatar_url")
    private String userAvatarUrl;
}
