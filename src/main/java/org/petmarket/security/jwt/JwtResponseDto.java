package org.petmarket.security.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponseDto {

    @JsonProperty("user_id")
    private Long userId;

    private String email;
    private String accessToken;
    private String refreshToken;

    @Schema(example = "andriy@mail.com", description = "Authenticated user email")
    public String getEmail() {
        return email;
    }

    @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBlbWFp" +
            "bC5jb20iLCJyb2xlcyI6WyJST0xFX0FETUlOIl0sImlhdCI" +
            "6MTY5MjAyMTE4NywiZXhwIjoxNjkyMDI0Nzg3fQ.cDhtb3UzxzYR3gPeIgCOTSaum-Z-yYHyF4VvhF0ND6M",
            description = "Authenticated user token")
    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getUserId() {
        return userId;
    }
}
