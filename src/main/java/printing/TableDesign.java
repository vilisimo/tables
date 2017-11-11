package printing;

import table.Table;

import static java.util.Collections.nCopies;
import static java.util.Objects.requireNonNull;
import static utils.Validations.requireLarger;

public class TableDesign {

    private static final String SEGMENT_DELIMITER = "";
    private static final int cornerWidth = 2;

    private final Table table;

    private char cornerSegment = '+';
    private char verticalSegment = '|';
    private char horizontalSegment = '-';

    private int cellPaddingWidth = 1;

    private String fancyBorder;
    private String plainBorder;

    public TableDesign(Table table) {
        requireNonNull(table, "Table cannot be null");

        this.table = table;

        this.fancyBorder = designBorder(cornerSegment);
        this.plainBorder = designBorder(horizontalSegment);
    }

    public TableDesign(Table table, char cornerSegment, char horizontalSegment, int cellPaddingWidth) {
        requireNonNull(table, "Table cannot be null");
        requireLarger(1, cellPaddingWidth, "Cell padding must be equal or greater than 0");

        this.table = table;

        this.cornerSegment = cornerSegment;
        this.horizontalSegment = horizontalSegment;
        this.cellPaddingWidth = cellPaddingWidth;

        this.fancyBorder = designBorder(cornerSegment);
        this.plainBorder = designBorder(horizontalSegment);

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

    /**
     * Returns a border with corners represented by special
     * characters.
     *
     * @return String representing a fancy border
     * @see TableDesign#setCornerSegment(char)
     */
    public String getFancyBorder() {
        return fancyBorder;
    }

    /**
     * Returns a plain border, where corners are the
     * same as other segments.
     *
     * @return String representing a plain border
     * @see TableDesign#setCornerSegment(char)
     */
    public String getPlainBorder() {
        return plainBorder;
    }

    private String designBorder(char corner) {
        int whitespacePadding = (cellPaddingWidth * 2) * table.columnCount();
        int borderPadding = table.columnCount() + 1;
        int totalWidth = table.getWidth() + whitespacePadding + borderPadding;
        int cornerlessWidth = totalWidth - cornerWidth;
        String horizontalFiller = String.join(
                SEGMENT_DELIMITER,
                nCopies(cornerlessWidth, String.valueOf(horizontalSegment)));

        return corner + horizontalFiller + corner;
    }

    public char getCornerSegment() {
        return cornerSegment;
    }

    public void setCornerSegment(char cornerSegment) {
        if (this.cornerSegment != cornerSegment) {
            this.cornerSegment = cornerSegment;
            this.fancyBorder = designBorder(cornerSegment);
        }
    }

    public char getVerticalSegment() {
        return verticalSegment;
    }

    public void setVerticalSegment(char verticalSegment) {
        this.verticalSegment = verticalSegment;
    }

    public char getHorizontalSegment() {
        return horizontalSegment;
    }

    public void setHorizontalSegment(char horizontalSegment) {
        if (this.horizontalSegment != horizontalSegment) {
            this.horizontalSegment = horizontalSegment;
            this.fancyBorder = designBorder(cornerSegment);
            this.plainBorder = designBorder(horizontalSegment);
        }
    }

    public int getCellPaddingWidth() {
        return cellPaddingWidth;
    }

    public void setCellPaddingWidth(int cellPaddingWidth) {
        requireLarger(0, cellPaddingWidth, "Cell padding must be equal to or greater than 0");
        if (this.cellPaddingWidth != cellPaddingWidth) {
            this.cellPaddingWidth = cellPaddingWidth;
            this.fancyBorder = designBorder(cornerSegment);
            this.plainBorder = designBorder(horizontalSegment);
        }
    }
}
