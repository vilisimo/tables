package utils;

import exceptions.EmptyCollection;
import exceptions.MismatchedSizes;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static utils.Validations.*;

public class ValidationsTest {

    @Test(expected = EmptyCollection.class)
    public void recognizesEmptyMap() {
        requireNonEmpty(Collections.emptyMap(), "test");
    }

    @Test
    public void allowsNonEmptyMap() {
        requireNonEmpty(Map.of("a", "b"), "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void recognizesNegativeNumber() {
        requireLarger(0, -1, "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotPermitLowerNumber() {
        requireLarger(10, 9, "test");
    }

    @Test
    public void allowsNonNegativeNumber() {
        requireLarger(0, 0, "test");
    }

    @Test
    public void allowsEqualNumber() {
        requireSmaller(0, 0, "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotPermitLargerNumber() {
        requireSmaller(9, 10, "test");
    }

    @Test(expected = MismatchedSizes.class)
    public void doesNotPermitListsOfDifferentSizes() {
        requireSameSize(List.of(
                List.of(1, 2, 3),
                List.of(1, 2)
        ), "test");
    }

    @Test
    public void allowsListsOfSameSize() {
        requireSameSize(List.of(
                List.of(1, 2),
                List.of(1, 2)
        ), "test");
    }

    @Test
    public void allowsEmptyList() {
        requireSameSize(List.of(), "test");
    }
}