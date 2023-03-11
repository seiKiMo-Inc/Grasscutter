package io.grasscutter.utils.exceptions;

import io.grasscutter.utils.objects.lang.TextContainer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/* Thrown when an invalid object is encountered. */
@Accessors(chain = true)
public final class InvalidException extends RuntimeException {
    @Getter private final String message;
    @Setter private Object[] args = {};

    public InvalidException(String message) {
        this.message = message;
    }

    @Override
    public String getLocalizedMessage() {
        return new TextContainer(this.message, this.args).toString();
    }
}
