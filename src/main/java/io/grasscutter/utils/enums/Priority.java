package io.grasscutter.utils.enums;

import java.util.List;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;

/* Basic priorities. */
@AllArgsConstructor
public enum Priority {
    LOWEST(0),
    LOW(1),
    NORMAL(2),
    HIGH(3),
    HIGHEST(4);

    @Getter private final int value;

    /**
     * Returns a list of priorities in order from highest to lowest.
     *
     * @return A list of priorities.
     */
    public static List<Priority> inOrder() {
        return Stream.of(Priority.values()).sorted((x, y) -> y.getValue() - x.getValue()).toList();
    }
}
