package printing;

import table.DataRow;
import table.Header;
import table.Table;
import utils.Containers;
import utils.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public class TablePrinter {

    private final Table table;
    private TableDesign design;

    public TablePrinter(Table table) {
        requireNonNull(table, "Table cannot be null");

        this.table = table;
        design = new TableDesign(table);
    }

    public TablePrinter(Table table, TableDesign design) {
        requireNonNull(table, "Table cannot be null");

        this.table = table;
        this.design = ofNullable(design).orElse(new TableDesign(table));
    }

    public void printTable() {
        printHeader();
        printRows();
    }

    private void printHeader() {
        System.out.println(design.getFancyBorder());
        printHeaderRow();
        System.out.println(design.getFancyBorder());
    }

    private void printHeaderRow() {
        Header header = table.getHeader();

        List<List<String>> columnRows = header.getColumns()
                .stream()
                .map(column -> Strings.chopString(column, header.getColumnWidth(column)))
                .collect(toList());

        List<String> rowRepresentations = processColumnRows(columnRows);
        rowRepresentations.forEach(System.out::println);
    }

    private void printRows() {
        List<DataRow> rows = table.getRows();
        for (int i = 0; i < rows.size(); i++) {
            printRow(rows.get(i));

            if (i == rows.size() - 1) {
                System.out.println(design.getFancyBorder());
            } else {
                System.out.println(design.getPlainBorder());
            }
        }
    }

    private void printRow(DataRow row) {
        Header header = table.getHeader();

        List<String> names = row.getColumnNames();
        List<String> columns = names
                .stream()
                .map(row::getColumnValue)
                .collect(Collectors.toList());

        List<List<String>> columnRows = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            columnRows.add(Strings.chopString(columns.get(i), header.getColumnWidth(names.get(i))));
        }

        List<String> rowRepresentations = processColumnRows(columnRows);
        rowRepresentations.forEach(System.out::println);
    }

    private List<String> processColumnRows(List<List<String>> columnRows) {
        Containers.normalizeListSizes(columnRows);
        columnRows = Containers.interleave(columnRows);

        return designRowRepresentations(columnRows);
    }

    private List<String> designRowRepresentations(List<List<String>> columnRows) {
        Header header = table.getHeader();

        List<String> rowRepresentations = new ArrayList<>();

        for (List<String> columnRow : columnRows) {
            List<String> borderlessRow = new ArrayList<>();
            for (int i = 0; i < columnRow.size(); i++) {
                String row = columnRow.get(i);
                int width = header.getColumnWidth(i);
                borderlessRow.add(designColumnString(row, width));
            }

            rowRepresentations.add(attachBorders(borderlessRow));
        }

        return rowRepresentations;
    }

    private String designColumnString(String column, int size) {
        String string = ofNullable(column).orElse("");

        StringBuilder builder = new StringBuilder();
        builder.append(Strings.repeat(' ', design.getCellPaddingWidth()));
        builder.append(string);
        builder.append(Strings.repeat(' ', size - string.length()));
        builder.append(Strings.repeat(' ', design.getCellPaddingWidth()));

        return builder.toString();
    }

    private String attachBorders(List<String> borderlessRows) {
        String verticalBorder = String.valueOf(design.getVerticalSegment());
        return borderlessRows.stream().collect(Collectors.joining(verticalBorder, verticalBorder, verticalBorder));
    }
}
