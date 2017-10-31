package utils;

import exceptions.EmptyCollection;
import exceptions.MismatchedSizes;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

public class ContainersTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void interleavesCollections() {
        List<List<Integer>> lists = List.of(
                List.of(1, 4),
                List.of(2, 5),
                List.of(3, 6)
        );

        List<List<Integer>> result = Containers.interleave(lists);

        assertThat(result, is(List.of(List.of(1, 2, 3), List.of(4, 5, 6))));
    }

    @Test(expected = MismatchedSizes.class)
    public void refusesListsOfDifferentSize() {
        List<List<Integer>> lists = List.of(
                List.of(1, 2, 3),
                List.of(1, 2));

        Containers.interleave(lists);
    }

    @Test(expected = EmptyCollection.class)
    public void refusesEmptyList() {
        Containers.interleave(List.of());
    }

    @Test
    public void interleaveDoesNotAllowNulls() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("List should not be null");
        Containers.interleave(null);
    }

    @Test
    public void normalizeCollectionsDoesNotAcceptNullCollection() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Collection should not be null");

        Containers.normalizeListSizes(null);
    }

    @Test
    public void normalizeCollectionsNormalizesLists() {
        List<String> list1 = new ArrayList<>();
        list1.add("a"); list1.add("b"); list1.add("c");
        List<String> list2 = new ArrayList<>();
        list2.add("d");

        List<List<String>> collections = List.of(list1, list2);

        Containers.normalizeListSizes(collections);

        assertThat(collections.get(1).size(), is(3));
        assertThat(collections.get(1).get(1), nullValue());
        assertThat(collections.get(1).get(2), nullValue());
    }

    @Test
    public void normalizeCollectionsReturnsSameCollectionsWhenCollectionSizesMatch() {
        List<String> list1 = new ArrayList<>();
        list1.add("a"); list1.add("b"); list1.add("c");
        List<String> list2 = new ArrayList<>();
        list2.add("d"); list2.add("e"); list2.add("f");

        List<List<String>> collections = List.of(list1, list2);

        Containers.normalizeListSizes(collections);

        assertThat(collections.get(1).size(), is(3));
        assertThat(collections.get(0), is(list1));
        assertThat(collections.get(1), is(list2));
    }

    @Test
    public void normalizeCollectionsReturnsEmptyCollectionWhenInputIsEmpty() {
        List<List<String>> collections = Collections.emptyList();

        Containers.normalizeListSizes(collections);

        assertThat(collections, is(empty()));
    }

    @Test
    public void figuresOutMaxCollectionSize() {
        int maxSize = Containers.maxListSize(List.of(
                List.of(1, 2,3 ),
                List.of(1, 2),
                List.of(1)));

        assertThat(maxSize, is(3));
    }

    @Test
    public void figuresOutMaxSizeWhenAllCollectionsAreOfSameSize() {
        int maxSize = Containers.maxListSize(List.of(
                List.of(1, 2),
                List.of(3, 4)));

        assertThat(maxSize, is(2));
    }

    @Test
    public void doesNotAcceptNullContainer() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Collection should not be null");

        Containers.maxListSize(null);
    }

    @Test(expected = EmptyCollection.class)
    public void doesNotAcceptEmptyContainer() {
        Containers.maxListSize(Collections.emptyList());
    }
}