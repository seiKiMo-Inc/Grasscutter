package io.grasscutter.game.data;

import io.grasscutter.utils.enums.Priority;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* A game resource. */
@Retention(RetentionPolicy.RUNTIME)
public @interface Resource {
    /*
     * Name of the resource file.
     */
    String[] name();

    /*
     * Changes when the resource will be loaded.
     */
    Priority priority() default Priority.NORMAL;
}
