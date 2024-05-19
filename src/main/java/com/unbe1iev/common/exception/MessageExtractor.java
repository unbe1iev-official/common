package com.unbe1iev.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
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

    public String getExceptionMessage(Exception exception, Object[] messageParameters) {
        return getMessage(exception.getClass().getName(), messageParameters);
    }

    public String getMessage(String code, Object[] messageParameters) {
        try {
            return messageSource.getMessage(code, messageParameters, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            log.error("No message for {}", code);
            return "";
        }
    }

    public String getExceptionMessage(MessageSourceResolvable resolvable) {
        return messageSource.getMessage(resolvable, LocaleContextHolder.getLocale());
    }
}
