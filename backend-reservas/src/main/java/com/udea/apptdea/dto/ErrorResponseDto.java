package com.udea.apptdea.dto;

import java.time.Instant;

/**
 * Standard error response DTO record returned on API exceptions.
 *
 * @param timestamp Time when the error occurred
 * @param status    HTTP status code
 * @param error     HTTP reason phrase
 * @param message   Descriptive error message
 * @param path      Request URI path
 */
public record ErrorResponseDto(
    Instant timestamp,
    int status,
    String error,
    String message,
    String path
) {
}

