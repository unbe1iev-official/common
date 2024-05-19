package com.unbe1iev.common.validator;

public interface Validator<D> {
    void validate(D toValidate);
}
