package table;

import exceptions.MismatchedColumns;
import exceptions.MissingHeader;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TableTest {

    private Header header;

    @Before
    public void setup() {
        LinkedHashMap<String, Integer> orderedMap = new LinkedHashMap<>();
        orderedMap.put("key1", 1);
        orderedMap.put("key2", 2);
        orderedMap.put("key3", 3);

        header = new Header(orderedMap);
    }

    @Test
    public void constructorSetsWidth() {
        Table table = new Table(header);

        assertThat(table.getWidth(), is(header.getWidth()));
    }

    @Test(expected = MissingHeader.class)
    public void constructorDoesNotAcceptNullHeader() {
        new Table(null);
    }

    @Test
    public void tableRecognizesWhenThereAreNoRows() {
        Table table = new Table(header);

        assertThat(table.isEmpty(), is(true));
    }

    @Test(expected = MismatchedColumns.class)
    public void tableRecognizesMismatchedHeaderAndDataRowSizes() {
        Table table = new Table(header);

        LinkedHashMap<String, String> columns = new LinkedHashMap<>();
        columns.put("key1", "val1");
        columns.put("key2", "val2");

        table.addRow(new DataRow(columns));
    }

    @Test(expected = MismatchedColumns.class)
    public void tableRecognizesRowsThatHaveValuesNotInTheHeader() {
        Table table = new Table(header);

        LinkedHashMap<String, String> rows = new LinkedHashMap<>();
        rows.put("key1", "val1");
        rows.put("key2", "val2");
        rows.put("key4", "val4");

        table.addRow(new DataRow(rows));
    }

    @Test
    public void returnsAllAddedRows() {
        Table table = new Table(header);

        LinkedHashMap<String, String> columns = new LinkedHashMap<>();
        columns.put("key1", "val1");
        columns.put("key2", "val2");
        columns.put("key3", "val3");

        table.addRow(new DataRow(columns));
        table.addRow(new DataRow(columns));
        table.addRow(new DataRow(columns));

        assertThat(table.getRows().size(), is(3));
    }

    @Test
    public void returnsColumnCountCorrespondingToHeader() {
        Table table = new Table(header);

        assertThat(table.columnCount(), is(header.columnCount()));
    }
}