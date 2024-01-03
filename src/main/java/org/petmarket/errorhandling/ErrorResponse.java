package org.petmarket.errorhandling;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    @Schema(example = "404", description = "Статус помилки")
    private int status;
    @Schema(example = "Error message", description = "Опис помилки")
    private String message;
    @Schema(example = "1691750778972", description = "Час помилки в мілісекундах")
    private long timestamp;
}
