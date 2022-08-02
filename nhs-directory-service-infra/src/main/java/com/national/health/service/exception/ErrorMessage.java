package com.national.health.service.exception;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public class ErrorMessage {
    private int statusCode;
    private LocalDate timestamp;
    private String message;
    private String description;
}
