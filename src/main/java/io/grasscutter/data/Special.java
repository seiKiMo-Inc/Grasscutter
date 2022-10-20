package io.grasscutter.data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* Marks the annotated field as "special". */
@Retention(RetentionPolicy.RUNTIME)
public @interface Special {
    FieldType value();
}
