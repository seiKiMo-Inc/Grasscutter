package io.grasscutter.utils.exceptions;

import io.grasscutter.utils.enums.CommandExceptionType;
import io.grasscutter.utils.objects.lang.TextContainer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/* Thrown during command validation. */
@Accessors(chain = true)
public final class CommandException extends RuntimeException {
    @Getter private final CommandExceptionType type;
    @Setter private Object[] args = {};

    public CommandException(CommandExceptionType type) {
        this.type = type;
    }

    @Override
    public String getLocalizedMessage() {
        return new TextContainer(type.getMessage(), this.args).toString();
    }
}
