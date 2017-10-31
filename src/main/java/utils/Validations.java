package utils;

import exceptions.EmptyCollection;
import exceptions.MismatchedSizes;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class Validations {

    private Validations() {
        throw new AssertionError("The class should not be instantiated");
    }

    public static <T, V> void requireNonEmpty(Map<T, V> map, String message) {
        if (map.isEmpty()) {
            throw new EmptyCollection(message);
        }
    }

    public static void requireNonEmpty(Collection collection, String message) {
        if (collection.isEmpty()) {
            throw new EmptyCollection(message);
        }
    }

    public static void requireLarger(int minimum, int actual, String message) {
        if (actual < minimum) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void requireSmaller(int maximum, int actual, String message) {
        if (actual > maximum) {
            throw new IllegalArgumentException(message);
        }
    }

    public static <T> void requireSameSize(List<List<T>> lists, String message) {
        if (lists.isEmpty()) {
            return;
        }

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (List<T> list : lists) {
            if (list.size() > max) {
                max = list.size();
            }
            if (list.size() < min) {
                min = list.size();
            }
        }

        if (min != max) {
            throw new MismatchedSizes(message);
        }
    }
}
