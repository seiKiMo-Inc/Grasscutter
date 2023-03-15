package io.grasscutter.utils.encodings;

import io.grasscutter.utils.PrimitiveUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.function.Function;

/* TSJ field parser. */
public final class FieldParser {
    public final Field field;
    public final Type type;
    public final Class<?> classType;
    public final Function<String, Object> parser;

    public FieldParser(Field field) {
        this.field = field;
        this.type = field.getGenericType();
        this.classType = field.getType();
        this.parser = PrimitiveUtils.getParser(this.type);
    }

    /**
     * Parses the token into an object.
     *
     * @param token The token to parse.
     * @return The parsed object.
     */
    public Object parse(String token) {
        return this.parser.apply(token);
    }

    /**
     * Parses the token into the target object.
     *
     * @param target The target object.
     * @param token The token to parse.
     * @throws IllegalAccessException If the target can't be accessed.
     */
    public void parse(Object target, String token) throws IllegalAccessException {
        this.field.set(target, this.parse(token));
    }
}
