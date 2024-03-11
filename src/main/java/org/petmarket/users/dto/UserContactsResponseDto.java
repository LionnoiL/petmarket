package org.petmarket.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@JsonPropertyOrder({"user_id", "email", "facebook_link", "instagram_link", "twitter_link", "main_phone", "phones"})
public class UserContactsResponseDto {

    @JsonProperty("user_id")
    private Long userId = 0L;

    @JsonProperty("facebook_link")
    private String facebookLink = "";

    @JsonProperty("instagram_link")
    private String instagramLink = "";

    @JsonProperty("twitter_link")
    private String twitterLink = "";

    private String email = "";

    @JsonProperty("main_phone")
    private String mainPhone = "";

    private Set<String> phones = new HashSet<>();
}
