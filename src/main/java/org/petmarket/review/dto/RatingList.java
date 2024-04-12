package org.petmarket.review.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingList {

    @JsonProperty("rating_1")
    private int rating1;

    @JsonProperty("rating_2")
    private int rating2;

    @JsonProperty("rating_3")
    private int rating3;

    @JsonProperty("rating_4")
    private int rating4;

    @JsonProperty("rating_5")
    private int rating5;

    public RatingList(int rating1, int rating2) {
        this.rating1 = rating1;
        this.rating2 = rating2;
    }

    public RatingList(List<Integer[]> ratings) {
        if (!ratings.isEmpty()) {
            Integer[] row = ratings.get(0);
            row = Arrays.stream(row)
                    .map(value -> value == null ? 0 : value)
                    .toArray(Integer[]::new);

            this.rating1 = row[0];
            this.rating2 = row[1];
            this.rating3 = row[2];
            this.rating4 = row[3];
            this.rating5 = row[4];
        }
    }
}
