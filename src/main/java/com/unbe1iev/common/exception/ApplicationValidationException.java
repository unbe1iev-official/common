package com.unbe1iev.common.exception;

import lombok.Getter;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;

@Getter
public class ApplicationValidationException extends DefaultRuntimeException {

    private final String[] messages;

    public ApplicationValidationException(List<ObjectError> objectErrors) {
        this.messages = prepareMessage(objectErrors);
    }

    public ApplicationValidationException(String message) {
        this.messages = new String[] { message };
    }

    @Override
    public String getMessage() {
        return String.join(",", this.messages);
    }

    private String[] prepareMessage(List<ObjectError> allErrors) {
        return allErrors.stream()
                .map(this::convertToString)
                .toArray(String[]::new);
    }

    private String convertToString(ObjectError objectError) {
        if (objectError instanceof FieldError fieldError) {
            return String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage());
        }
        return objectError.getDefaultMessage();
    }
}