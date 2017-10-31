package printing;

import table.DataRow;
import table.Header;
import table.Table;
import utils.Containers;
import utils.Strings;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static utils.Validations.requireLarger;

public class TablePrinter {

    private final Table table;

    private final int cellPadding;

    private String fancyBorder;
    private String simpleBorder;

    public TablePrinter(Table table) {
        this.cellPadding = 1;
        this.table = table;
        this.fancyBorder = designBorder('+');
        this.simpleBorder = designBorder('-');
    }

    private String designBorder(char corner) {
        int whitespacePadding = (cellPadding * table.columnCount()) * 2;
        int borderPadding = table.columnCount() + 1;
        int totalWidth = table.getWidth() + whitespacePadding + borderPadding;
        int cornerWidth = 2;
        String horizontalFiller = String.join("", Collections.nCopies(totalWidth - cornerWidth, "-"));

        return corner + horizontalFiller + corner;
    }

    public static int usableWidth(int tableWidth, int singlePadWidth, int columns) {
        requireLarger(0, singlePadWidth, "Width of the padding cannot be negative [entered=" + singlePadWidth + "]");
        requireLarger(1, tableWidth, "Table width cannot be less than 1 [entered=" + tableWidth + "]");
        requireLarger(1, columns, "Table cannot have less columns than 1 [entered=" + columns + "]");

        int bordersWidth = columns + 1;
        int totalPaddingWidth = (singlePadWidth * 2) * columns;
        int usableWidth = tableWidth - bordersWidth - totalPaddingWidth;

        if (usableWidth < 0) {
            return 0;
        }

        return  usableWidth;
    }

    public void printTable() {
        printHeader();
        printRows();
    }

    private void printHeader() {
        System.out.println(fancyBorder);
        printHeaderRow();
        System.out.println(fancyBorder);
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
                System.out.println(fancyBorder);
            } else {
                System.out.println(simpleBorder);
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
        String string = Optional.ofNullable(column).orElse("");

        StringBuilder builder = new StringBuilder();
        builder.append(Strings.repeat(' ', cellPadding));
        builder.append(string);
        builder.append(Strings.repeat(' ', size - string.length()));
        builder.append(Strings.repeat(' ', cellPadding));

        return builder.toString();
    }

    private String attachBorders(List<String> borderlessRows) {
        return borderlessRows.stream().collect(Collectors.joining("|", "|", "|"));
    }

    public static void main(String[] args) {
        int usableWidth = TablePrinter.usableWidth(79, 1, 3);
        int titleLength = 4;
        int deadlineLength = "2018-01-11".length();
        int descriptionLength = usableWidth - titleLength - deadlineLength - deadlineLength;

        LinkedHashMap<String, Integer> headerColumns = new LinkedHashMap<>();
        headerColumns.put("Title", titleLength);
        headerColumns.put("Description", descriptionLength);
        headerColumns.put("Created", deadlineLength);
        headerColumns.put("Deadline", deadlineLength);
        Header header = new Header(headerColumns);

        LinkedHashMap<String, String> rowColumns = new LinkedHashMap<>();
        rowColumns.put("Title", "test1");
        rowColumns.put("Description", "Some really long description that has no way of fitting in one line and perhaps even not fitting on two lines or even three");
        rowColumns.put("Created", "2017-11-11");
        rowColumns.put("Deadline", "2018-05-11");
        DataRow row = new DataRow(rowColumns);

        LinkedHashMap<String, String> rowColumns2 = new LinkedHashMap<>();
        rowColumns2.put("Title", "test2");
        rowColumns2.put("Description", "Shorter description that definitely fits on two lines");
        rowColumns2.put("Created", "2011-11-11");
        rowColumns2.put("Deadline", "2015-01-10");
        DataRow row2 = new DataRow(rowColumns2);

        Table table = new Table(header);
        table.addRow(row);
        table.addRow(row2);
        TablePrinter printer = new TablePrinter(table);
        printer.printTable();
    }
}
