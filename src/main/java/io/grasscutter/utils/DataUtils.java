package io.grasscutter.utils;

/* Utility methods to manipulate data. */
public interface DataUtils {
    /**
     * Fallback method.
     *
     * @param left Attempt to use.
     * @param right Use if left is undefined.
     * @return Left or right.
     */
    static <T> T lr(T left, T right) {
        return left == null ? right : left;
    }

    /**
     * {@link DataUtils#lr(Object, Object)} for {@link String}s.
     *
     * @param left Attempt to use.
     * @param right Use if left is empty.
     * @return Left or right.
     */
    static String lr(String left, String right) {
        return left == null || left.isEmpty() ? right : left;
    }

    /**
     * {@link DataUtils#lr(Object, Object)} for {@link Integer}s.
     *
     * @param left Attempt to use.
     * @param right Use if left is 0.
     * @return Left or right.
     */
    static int lr(int left, int right) {
        return left == 0 ? right : left;
    }

    /**
     * Checks if the given object is a primitive.
     *
     * @param object The object to check.
     * @return True if the object is a primitive.
     */
    static boolean isPrimitive(Object object) {
        return object instanceof String
                || object instanceof Integer
                || object instanceof Long
                || object instanceof Double
                || object instanceof Float
                || object instanceof Boolean;
    }
}
