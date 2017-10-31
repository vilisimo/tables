package printing;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TablePrinterTest {

    @Test
    public void calculatesUsableWidth() {
        int table = 80;
        int columns = 5;
        int singlePad = 1;
        int borders = columns + 1;
        int padding = (singlePad * 2) * columns;
        int freeWidth = table - borders - padding;

        int usableWidth = TablePrinter.usableWidth(table, singlePad, columns);

        assertThat(usableWidth, is(freeWidth));
    }

    @Test
    public void returnsZeroWhenUsableSizeNegative() {
        int usableWidth = TablePrinter.usableWidth(1, 1, 5);

        assertThat(usableWidth, is(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesNegativePadWidth() {
        TablePrinter.usableWidth(1, -1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesNonPositiveTableWidth() {
        TablePrinter.usableWidth(0, 1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesNonPositiveColumns() {
        TablePrinter.usableWidth(100, 1, 0);
    }
}