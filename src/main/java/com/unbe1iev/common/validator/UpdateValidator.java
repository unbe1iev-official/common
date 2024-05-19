package com.unbe1iev.common.validator;

public interface UpdateValidator<ID, D> {
    void validate(ID id, D toValidate);
}
