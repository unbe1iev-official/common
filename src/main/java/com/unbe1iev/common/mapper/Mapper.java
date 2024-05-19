package com.unbe1iev.common.mapper;

import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface Mapper<I, O> {

    @NonNull
    O map(@NonNull I inputObject);

    default @NonNull
    List<O> map(@NonNull Collection<I> inputList) {
        return inputList.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }
}
