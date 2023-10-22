package org.petmarket.utils;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorUtils {

    public static String getErrorsString(Errors bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            errorMessage.append(error.getField())
                .append(" - ")
                .append(error.getDefaultMessage())
                .append("; ");
        }
        return errorMessage.toString();
    }
}
