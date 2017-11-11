package printing;

import org.junit.Before;
import org.junit.Test;
import table.Header;
import table.Table;

import java.util.LinkedHashMap;

import static java.util.Collections.nCopies;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TableDesignTest {

    private Table table;
    private TableDesign design;

    @Before
    public void setup() {
        LinkedHashMap<String, Integer> columns = new LinkedHashMap<>();
        columns.put("ID", 4);
        columns.put("Title", 6);
        Header header = new Header(columns);

        table = new Table(header);
        design = new TableDesign(table);
    }

    @Test(expected = NullPointerException.class)
    public void doesNotAllowNullTable() {
        new TableDesign(null);
    }

    @Test(expected = NullPointerException.class)
    public void doesNotAllowNullTableInOverloadedConstructor() {
        new TableDesign(null, '-', '-', 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAllowNegativePaddingInOverloadedConstructor() {
        new TableDesign(table, '0', '1', -1);
    }


    @Test
    public void calculatesUsableWidth() {
        int table = 80;
        int columns = 5;
        int singlePad = 1;
        int borders = columns + 1;
        int padding = (singlePad * 2) * columns;
        int freeWidth = table - borders - padding;

        int usableWidth = TableDesign.usableWidth(table, singlePad, columns);

        assertThat(usableWidth, is(freeWidth));
    }

    @Test
    public void returnsZeroWhenUsableSizeNegative() {
        int usableWidth = TableDesign.usableWidth(1, 1, 5);

        assertThat(usableWidth, is(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesNegativePadWidth() {
        TableDesign.usableWidth(1, -1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesNonPositiveTableWidth() {
        TableDesign.usableWidth(0, 1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesNonPositiveColumns() {
        TableDesign.usableWidth(100, 1, 0);
    }

    @Test
    public void fancyBorderHasSpecialCorners() {
        design.setCornerSegment('+');
        String fancyBorder = design.getFancyBorder();

        assertThat(fancyBorder.charAt(0), is('+'));
        assertThat(fancyBorder.charAt(fancyBorder.length() - 1), is('+'));
    }

    @Test
    public void fancyBorderHasPlainMiddleSegments() {
        design.setCornerSegment('*');
        design.setHorizontalSegment('a');
        int columnPadding = table.columnCount() * 2;
        int borderWidth = table.columnCount() + 1;
        String expected = String.join("", nCopies(table.getWidth() + columnPadding + borderWidth, "a"));
        expected = '*' + expected.substring(1, expected.length() - 1) + '*';

        String fancyBorder = design.getFancyBorder();

        assertThat(fancyBorder, is(expected));
    }

    @Test
    public void plainBorderHasPlainSegments() {
        design.setHorizontalSegment('/');
        int columnPadding = table.columnCount() * 2;
        int borderWidth = table.columnCount() + 1;
        String expected = String.join("", nCopies(table.getWidth() + columnPadding + borderWidth, "/"));

        String plainBorder = design.getPlainBorder();

        assertThat(plainBorder, is(expected));
    }

    @Test
    public void paddingWidthAffectsBorderLength() {
        design.setCellPaddingWidth(0);
        int beforePadding = design.getPlainBorder().length();

        int newWidth = 2;
        design.setCellPaddingWidth(newWidth);
        int afterPadding = design.getPlainBorder().length();

        int sides = 2;
        int totalPadding = (newWidth * sides) * table.columnCount();
        int expectedWidth = beforePadding + totalPadding;

        assertThat(afterPadding, is(expectedWidth));
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAllowToSetNegativeCellPadding() {
        design.setCellPaddingWidth(-1);
    }
}