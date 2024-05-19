package com.unbe1iev.common.util;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class TotalOverridePageImpl<T> extends PageImpl<T> {

    private final long totalElements;

    public TotalOverridePageImpl(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
        this.totalElements = total;
    }

    @Override
    public long getTotalElements() {
        return this.totalElements;
    }
}
