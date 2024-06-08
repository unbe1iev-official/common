package com.unbe1iev.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageExtractor {

    private final MessageSource messageSource;

    public MessageExtractor(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String[] getExceptionMessage(Exception exception, Object[] messageParameters) {
        return new String[]{getMessage(exception.getClass().getName(), messageParameters)};
    }

    public String getMessage(String code, Object[] messageParameters) {
        try {
            String message = messageSource.getMessage(code, messageParameters, LocaleContextHolder.getLocale());
            log.debug("Retrieved message for code {}: {}", code, message);
            return message;
        } catch (NoSuchMessageException e) {
            log.error("No message for {}", code);
            return "Access Denied";
        }
    }
}
