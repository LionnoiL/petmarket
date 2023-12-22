package org.petmarket.utils.annotations;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_OPERATION;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION)
public @interface ApiResponseSuccessful {
}
