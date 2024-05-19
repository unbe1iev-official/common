package com.unbe1iev.common.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ErrorDto {

    String id;

    int status;

    String[] messages;

    LocalDateTime timestamp;
}
