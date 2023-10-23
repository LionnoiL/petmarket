package org.petmarket.advertisements.advertisement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponseDto {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String site;
    private int rating;

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getSite() {
        return site;
    }

    public int getRating() {
        return rating;
    }
}
