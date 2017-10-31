package table;

import exceptions.EmptyCollection;
import exceptions.MissingColumn;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class HeaderTest {

    private Header header;

    @Before
    public void setup() {

        LinkedHashMap<String, Integer> columns = new LinkedHashMap<>();
        columns.put("key1", 1);
        columns.put("key2", 2);
        columns.put("key3", 3);

        header = new Header(columns);
    }

    @Test
    public void constructorSetsWidth() {
        assertThat(header.getWidth(), is(6));
    }

    @Test(expected = EmptyCollection.class)
    public void constructorRefusesEmptyHeader() {
        new Header(new LinkedHashMap<>());
    }

    @Test(expected = NullPointerException.class)
    public void constructorRefusesNullMap() {
        new Header(null);
    }

    @Test
    public void returnsListOfKeys() {
        List<String> headerColumns = header.getColumns();

        assertThat(headerColumns, containsInAnyOrder("key1", "key2", "key3"));
    }

    @Test
    public void orderedMapReturnsListWithSameOrderOfKeys() {
        List<String> headerColumns = header.getColumns();

        assertThat(headerColumns, is(List.of("key1", "key2", "key3")));
    }

    @Test
    public void returnsColumnWidth() {
        assertThat(header.getColumnWidth("key1"), is(1));
        assertThat(header.getColumnWidth("key2"), is(2));
        assertThat(header.getColumnWidth("key3"), is(3));
    }

    @Test(expected = MissingColumn.class)
    public void recognizesMissingColumnWhenAskedForWidth() {
        header.getColumnWidth("key4");
    }

    @Test
    public void recognizesMissingColumn() {
        assertThat(header.containsColumn("key2"), is(true));
        assertThat(header.containsColumn("key4"), is(false));
    }

    @Test
    public void containsAllRecognizesAllValues() {
        assertThat(header.containsColumns(List.of("key1", "key2", "key3")), is(true));
    }

    @Test
    public void containsAllRecognizesMissingValues() {
        assertThat(header.containsColumns(List.of("key1", "key2", "key3", "key4")), is(false));
    }

    @Test(expected = NullPointerException.class)
    public void doesNotAcceptNullValues() {
        header.containsColumns(null);
    }

    @Test
    public void returnsColumnCount() {
        assertThat(header.columnCount(), is(3));
    }

    @Test
    public void doesNotAllowToModifyMapAfterAddingItToHeader() {
        LinkedHashMap<String, Integer> columns = new LinkedHashMap<>();
        columns.put("key1", 1);

        Header header = new Header(columns);
        columns.put("key2", 2);

        assertThat(header.columnCount(), is(1));
    }

    @Test
    public void retrievesCorrectValuesBasedOnColumnPosition() {
        assertThat(header.getColumnWidth(0), is(1));
        assertThat(header.getColumnWidth(1), is(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptNegativeColumnPosition() {
        header.getColumnWidth(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptLargerThanSizePositions() {
        LinkedHashMap<String, Integer> columns = new LinkedHashMap<>();
        columns.put("key1", 1);

        Header header = new Header(columns);

        header.getColumnWidth(1);
    }
}