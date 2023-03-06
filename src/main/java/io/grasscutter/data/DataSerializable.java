package io.grasscutter.data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* Marks the annotated class as savable to the database. */
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSerializable {
    /* The name of the table to save to. */
    String table();
}
