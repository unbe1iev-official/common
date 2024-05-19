package com.unbe1iev.common.util;

import com.unbe1iev.common.mapper.Mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PageUtil {

    public static <D> Page<D> getPagedResult(Pageable pageable, List<D> result) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), result.size());
        List<D> pagedList = result.subList(start, end);

        return new PageImpl<>(pagedList, pageable, result.size());
    }

    public static <O, I> Page<O> convert(Page<I> contentVersionPage, Mapper<I, O> converter) {
        return new PageImpl<>(converter.map(contentVersionPage.getContent()), contentVersionPage.getPageable(), contentVersionPage.getTotalElements());
    }
}
