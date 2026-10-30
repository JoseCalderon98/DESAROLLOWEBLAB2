package com.udea.banktdea.dto;

import java.time.Instant;

/**
 * Standard error response DTO record returned on API exceptions.
 *
 * @param timestamp Time of occurrence
 * @param status    HTTP status code
 * @param error     HTTP status error description
 * @param message   Error explanation
 * @param path      Request URI
 */
public record ErrorResponseDto(
    Instant timestamp,
    int status,
    String error,
    String message,
    String path
) {
}

