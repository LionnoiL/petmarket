package org.petmarket.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.petmarket.errorhandling.ItemNotCreatedException;
import org.petmarket.errorhandling.ItemNotUpdatedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.List;

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

    public static void checkItemNotCreatedException(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ItemNotCreatedException(getErrorsString(bindingResult));
        }
    }

    public static void checkItemNotUpdatedException(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ItemNotUpdatedException(getErrorsString(bindingResult));
        }
    }
}
