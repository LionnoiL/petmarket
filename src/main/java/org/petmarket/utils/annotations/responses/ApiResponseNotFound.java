package org.petmarket.utils.annotations.responses;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.petmarket.errorhandling.ErrorResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.petmarket.utils.MessageUtils.NOT_FOUND;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(responseCode = "404", description = NOT_FOUND, content = {
        @Content(mediaType = "application/json", schema =
        @Schema(implementation = ErrorResponse.class))
})
public @interface ApiResponseNotFound {
}
