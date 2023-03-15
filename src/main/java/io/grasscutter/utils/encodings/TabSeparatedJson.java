package io.grasscutter.utils.encodings;

import com.google.gson.annotations.SerializedName;
import io.grasscutter.utils.PrimitiveUtils;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* The TSJ encoding. */
public interface TabSeparatedJson {
    Map<Class<?>, Map<String, FieldParser>> CLASS_FIELDS = new HashMap<>();

    /**
     * Creates a field parser cache for the class.
     *
     * @param classType The class type.
     * @return The field parser cache.
     */
    static Map<String, FieldParser> cacheClassFields(Class<?> classType) {
        var fieldMap = new HashMap<String, FieldParser>();

        // Get all fields in the class.
        for (var field : classType.getDeclaredFields()) {
            field.setAccessible(true);
            var fieldParser = new FieldParser(field);

            var serialized = field.getDeclaredAnnotation(SerializedName.class);
            if (serialized == null) {
                // Put a raw field name with a parser.
                fieldMap.put(field.getName(), fieldParser);
            } else {
                // Put a parser with the serialized name.
                fieldMap.put(serialized.value(), fieldParser);
                // Repeat this with all alternate names.
                for (var alt : serialized.alternate()) {
                    fieldMap.put(alt, fieldParser);
                }
            }

            field.setAccessible(false);
        }

        return fieldMap;
    }

    /**
     * Gets a cached field map for a class type.
     *
     * @param classType The class type.
     * @return The field map.
     */
    static Map<String, FieldParser> getClassFieldMap(Class<?> classType) {
        return CLASS_FIELDS.computeIfAbsent(classType, TabSeparatedJson::cacheClassFields);
    }

    /**
     * Decodes a file to a list.
     *
     * @param file The file to decode.
     * @param type The type of the list.
     * @return The decoded list.
     */
    static <T> List<T> toList(Path file, Class<T> type) {
        try (var fileReader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            var fieldMap = TabSeparatedJson.getClassFieldMap(type);
            var constructor = type.getDeclaredConstructor();

            // Parse the file content.
            var headerNames = PrimitiveUtils.split(fileReader.readLine(), '\t');
            var columns = headerNames.size();
            var fieldParsers = headerNames.stream().map(fieldMap::get).toList();

            return fileReader
                    .lines()
                    .parallel()
                    .map(
                            line -> {
                                var tokens = PrimitiveUtils.split(line, '\t');
                                var min = Math.min(tokens.size(), columns);

                                try {
                                    var obj = constructor.newInstance();

                                    // Attempt to parse each JSON object.
                                    for (var t = 0; t < min; t++) {
                                        var fieldParser = fieldParsers.get(t);
                                        if (fieldParser == null) continue;

                                        var token = tokens.get(t);
                                        if (!token.isEmpty()) {
                                            fieldParser.parse(obj, token);
                                        }
                                    }

                                    return obj;
                                } catch (Exception ignored) {
                                    return null;
                                }
                            })
                    .toList();
        } catch (Exception ignored) {
            return null;
        }
    }
}
