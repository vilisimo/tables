package utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static utils.Validations.requireNonEmpty;
import static utils.Validations.requireSameSize;

public final class Containers {

    private Containers() {
        throw new AssertionError("The class should not be instantiated");
    }

    /**
     * Makes each collection of the same size by adding a specified filler.
     *
     * IMPORTANT: the lists WILL contain null values after normalization,
     * unless they are already of the same size. Hence, it is imperative
     * to use {@code Optional.ofNullable(T val).orElse(T other)} to avoid NPEs.
     */
    public static <T> void normalizeListSizes(List<List<T>> lists) {
        requireNonNull(lists, "Collection should not be null");

        if (lists.isEmpty()) {
            return;
        }

        int maxSize = maxListSize(lists);

        for (List<T> list : lists) {
            for (int i = list.size(); i < maxSize; i++) {
                list.add(null); //list?
            }
        }
    }

    public static <T> int maxListSize(List<List<T>> lists) {
        requireNonNull(lists, "Collection should not be null");
        requireNonEmpty(lists, "Collection should not be empty");

        int size = 0;
        for (Collection collection : lists) {
            if (collection.size() > size) {
                size = collection.size();
            }
        }

        return size;
    }

    /**
     * Takes a bunch of lists and reorganizes them to form "rows".
     * That is, the resulting list of lists will have as many lists
     * as there are elements in the primal lists. Those "rows" will
     * have 1..n-th elements from each list.
     *
     * Each list represents a row in an n-th column.
     */
    public static <T> List<List<T>> interleave(List<List<T>> lists) {
        requireNonNull(lists, "List should not be null");
        requireNonEmpty(lists, "List should not be empty");
        requireSameSize(lists, "Lists should be of the same size");

        int iterations = lists.get(0).size();

        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
            List<T> intermediate = new ArrayList<>();
            for (List<T> list : lists) {
                intermediate.add(list.get(i));
            }
            result.add(intermediate);
        }

        return result;
    }
}
