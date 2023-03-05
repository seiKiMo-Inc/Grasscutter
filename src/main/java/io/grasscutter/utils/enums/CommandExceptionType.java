package io.grasscutter.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public enum CommandExceptionType {
    NOT_FOUND("command.exception.not_found"),
    INVALID_USAGE(""),
    INVALID_ARGUMENT(""),
    INVALID_PERMISSION(""),
    INVALID_SENDER("");

    final String message;
}
