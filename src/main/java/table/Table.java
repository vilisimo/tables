package table;

import exceptions.MismatchedColumns;
import exceptions.MissingHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Table {

    private final Header header;
    private List<DataRow> rows;
    private final int width;

    public Table(Header header) {
        this.header = Optional.ofNullable(header).orElseThrow(() -> new MissingHeader("Table must have a header"));
        this.width = header.getWidth();
        this.rows = new ArrayList<>();
    }

    public void addRow(DataRow row) {
        requireSameSize(row);
        requireMatchingColumns(row);

        rows.add(row);
    }

    private void requireSameSize(DataRow row) {
        if (header.columnCount() != row.columnCount()) {
            throw new MismatchedColumns("Header (" + header.columnCount() +") " +
                    "and row (" + row.columnCount() + ") column count does not match");
        }
    }

    private void requireMatchingColumns(DataRow row) {
        List<String> rowColumns = row.getColumnNames();

        for (String column : rowColumns) {
            if (!header.containsColumn(column)) {
                throw new MismatchedColumns("Value is missing from the header: " + column);
            }
        }
    }

    public int columnCount() {
        return header.columnCount();
    }

    /**
     * Answers the question of whether the table has any rows.
     *
     * @return whether the table has rows
     */
    public boolean isEmpty() {
        return rows.isEmpty();
    }

    public Header getHeader() {
        return header;
    }

    public List<DataRow> getRows() {
        return rows;
    }

    public int getWidth() {
        return width;
    }
}
