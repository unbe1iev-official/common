package com.unbe1iev.common.exception;

import org.springframework.context.MessageSourceResolvable;

public abstract class DefaultRuntimeException extends RuntimeException implements MessageSourceResolvable {

    public DefaultRuntimeException() {
    }

    public DefaultRuntimeException(Throwable cause) {
        super(cause);
    }

    @Override
    public String[] getCodes() {
        return new String[] { this.getClass().getName() };
    }

    @Override
    public Object[] getArguments() {
        return new Object[0];
    }

    @Override
    public String getDefaultMessage() {
        return this.getClass().getName();
    }
}
