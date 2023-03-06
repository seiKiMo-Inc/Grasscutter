package io.grasscutter.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommandExceptionType {
    NOT_FOUND("command.exception.not_found"),
    ERROR_EXECUTING("command.exception.execute"),
    INVALID_USAGE("command.exception.usage"),
    INVALID_ARGUMENT("command.exception.argument"),
    INVALID_PERMISSION("command.exception.permission"),
    INVALID_SENDER("command.exception.sender");

    final String message;
}
