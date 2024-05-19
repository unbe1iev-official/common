package com.unbe1iev.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@ControllerAdvice
@Slf4j
public class DefaultExceptionHandler {

    private static final String INTERNAL_SERVER_ERROR_MESSAGE_CODE = "internal.server.error";
    public static final String DEV_PROFILE = "dev";

    private final MessageExtractor messageExtractor;
    private final boolean isDevelopmentMode;

    public DefaultExceptionHandler(MessageExtractor messageExtractor,
                                   Environment environment) {
        this.messageExtractor = messageExtractor;
        this.isDevelopmentMode = DEV_PROFILE.equalsIgnoreCase(environment.getActiveProfiles().length > 0 ? environment.getActiveProfiles()[0] : "");
    }

    @ExceptionHandler({
            DefaultRuntimeException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDto handleDefaultRuntimeException(WebRequest req, DefaultRuntimeException e) {
        return processException(HttpStatus.BAD_REQUEST.value(), e, messageExtractor.getExceptionMessage(e));
    }

    @ExceptionHandler({
            ApplicationValidationException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDto handleDefaultRuntimeException(WebRequest req, ApplicationValidationException e) {
        return processException(HttpStatus.BAD_REQUEST.value(), e, e.getMessages());
    }

    @ExceptionHandler(value = NoSuchElementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDto handleNoSuchElementException(WebRequest req, NoSuchElementException e) {
        return processException(HttpStatus.BAD_REQUEST.value(), e, messageExtractor.getExceptionMessage(e, new Object[]{e.getMessage()}));
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorDto handleNoSuchElementException(WebRequest req, AccessDeniedException e) {
        return processException(HttpStatus.UNAUTHORIZED.value(), e, messageExtractor.getExceptionMessage(e, new Object[]{e.getMessage()}));
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDto handleException(WebRequest req, Exception e) {
        String[] messages;
        if (isDevelopmentMode) {
            messages = new String[]{
                    messageExtractor.getMessage(INTERNAL_SERVER_ERROR_MESSAGE_CODE, new Object[]{}),
                    e.getMessage() != null ? e.getMessage() : e.toString()};
        } else {
            messages = new String[]{
                    messageExtractor.getMessage(INTERNAL_SERVER_ERROR_MESSAGE_CODE, new Object[]{})};
        }
        return processException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e, messages);
    }

    private ErrorDto processException(int status, Exception e, String message) {

        return processException(status, e, new String[]{message});
    }

    private ErrorDto processException(int status, Exception e, String[] messages) {
        String uuid = UUID.randomUUID().toString();
        log.error("{}", uuid, e);
        return getExceptionDto(status, messages, uuid);
    }

    private ErrorDto getExceptionDto(int status, String[] messages, String uuid) {
        return ErrorDto.builder()
                .id(uuid)
                .status(status)
                .messages(messages)
                .timestamp(LocalDateTime.now())
                .build();

    }
}
