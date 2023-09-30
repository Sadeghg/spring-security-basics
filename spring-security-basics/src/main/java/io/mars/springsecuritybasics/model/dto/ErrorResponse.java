package io.mars.springsecuritybasics.model.dto;

import org.springframework.http.HttpStatus;

public record ErrorResponse(HttpStatus status, String message) {
}
