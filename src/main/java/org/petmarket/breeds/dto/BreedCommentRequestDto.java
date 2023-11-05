package org.petmarket.breeds.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BreedCommentRequestDto {
    @NotNull
    @Schema (description = "Breed comment", example = "Думаю шо це гарна ідея купити таку тварину")
    private String comment;
}
