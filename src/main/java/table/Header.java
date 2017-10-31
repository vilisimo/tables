package table;

import exceptions.MissingColumn;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static utils.Validations.*;

public class Header {

    private final LinkedHashMap<String, Integer> columns;
    private final int width;

    public Header(LinkedHashMap<String, Integer> columns) {
        requireNonNull(columns, "Header columns cannot be null");
        requireNonEmpty(columns, "Header columns cannot be empty");

        this.width = columns.values().stream().mapToInt(Number::intValue).sum();
        this.columns = new LinkedHashMap<>(columns);
    }

    public List<String> getColumns() {
        return columns.entrySet()
                .stream()
                .map(Map.Entry::getKey)
                .collect(toList());
    }

    public int getColumnWidth(String column) {
        if (!this.containsColumn(column)) {
            throw new MissingColumn("Header does not have specified column ('" + column + "')");
        }

        return columns.get(column);
    }

    /**
     * Get column by its position in the header.
     *
     * Numbering starts at 0.
     */
    public int getColumnWidth(int columnPosition) {
        requireLarger(0, columnPosition, "Column position [" + columnPosition + "] cannot be negative");
        requireSmaller(columns.size() - 1, columnPosition, "Column position [\" + columnPosition + \"] is too large.");

        List<String> columnTitles = getColumns();
        String columnString = columnTitles.get(columnPosition);

        return columns.get(columnString);
    }

    public boolean containsColumn(String column) {
        return columns.containsKey(column);
    }

    public boolean containsColumns(List<String> columnNames) {
        requireNonNull(columnNames, "Supplied collection of column names should not be null");

        return columns.keySet().containsAll(columnNames);
    }

    public int columnCount() {
        return columns.size();
    }

    public int getWidth() {
        return width;
    }
}
