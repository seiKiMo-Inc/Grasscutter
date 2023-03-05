package io.grasscutter.utils.exceptions;

import io.grasscutter.utils.enums.CommandExceptionType;
import io.grasscutter.utils.objects.lang.TextContainer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class CommandException extends RuntimeException {
    @Getter private final CommandExceptionType type;

    @Override
    public String getMessage() {
        return new TextContainer(type.getMessage()).toString();
    }
}
