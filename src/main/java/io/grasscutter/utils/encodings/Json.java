package io.grasscutter.utils.encodings;

import com.google.gson.reflect.TypeToken;
import io.grasscutter.utils.EncodingUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/* The JSON encoding. */
public interface Json {
    /**
     * Decodes a file to a list.
     *
     * @param file The file to decode.
     * @param type The type of the list.
     * @return The decoded list.
     * @throws IOException If an I/O error occurs.
     */
    static <T> List<T> toList(Path file, Class<T> type) throws IOException {
        try (var reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            return EncodingUtils.fromJson(reader, TypeToken.getParameterized(List.class, type).getType());
        }
    }
}
