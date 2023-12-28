package org.petmarket.utils.annotations.responses;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_DELETED;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(responseCode = "204", description = SUCCESSFULLY_DELETED)
public @interface ApiResponseDeleted {
}
