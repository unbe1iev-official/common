package com.unbe1iev.common.validator.impl;

import com.unbe1iev.common.exception.ApplicationValidationException;
import com.unbe1iev.common.exception.MessageExtractor;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class CommonValidatorStepsImpl {

    private final MessageExtractor messageExtractor;

    protected void throwMessage(String code, Object... messageParameters) {
        String message = messageExtractor.getMessage(code, messageParameters);
        throw new ApplicationValidationException(message);
    }
}
