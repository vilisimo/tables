package table;

import exceptions.EmptyCollection;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class DataRowTest {

    private DataRow row;

    @Before
    public void setup() {
        LinkedHashMap<String, String> columns = new LinkedHashMap<>();
        columns.put("key1", "val1");
        columns.put("key2", "val2");

        row = new DataRow(columns);
    }


    @Test(expected = EmptyCollection.class)
    public void constructorRefusesEmptyMap() {
        new DataRow(new LinkedHashMap<>());
    }

    @Test
    public void returnsListOfColumns() {
        assertThat(row.getColumnNames(), containsInAnyOrder("key1", "key2"));
    }

    @Test
    public void returnsListInOrderItWasAddedIfOrderedMapIsUsed() {
        assertThat(row.getColumnNames(), is(List.of("key1", "key2")));
    }

    @Test
    public void returnsColumnCount() {
        assertThat(row.columnCount(), is(2));
    }

    @Test
    public void returnsColumnValue() {
        assertThat(row.getColumnValue("key1"), is("val1"));
    }

    @Test
    public void doesNotAllowToModifyMapAfterAddingItToRow() {
        LinkedHashMap<String, String> columns = new LinkedHashMap<>();
        columns.put("key1", "val1");

        DataRow header = new DataRow(columns);
        columns.put("key2", "val2");

        assertThat(header.columnCount(), is(1));
    }
}