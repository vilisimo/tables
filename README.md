# Tables
`tables` is a library that provides basic tools to build a table and print it to your terminal.

It was extracted from [tasks](https://github.com/vilisimo/tasks) project, when building 
and printing a table proved to be more tricky than the project itself. 

# Usage
## Constructing a table
To print a table, one has to have a table. It can be created like so:
~~~
Table table = new Table(header);
~~~

Table must have a header. Header determines the number of columns the table will contain.
This number is fixed. There can be no more and no less columns.

Header also dictates the width of a table. Note that the table is blissfully unaware of 
borders or padding - this is entirely up to `TablePrinter` to decide.   

### Header
To create a header:
~~~
LinkedHashMap<String, Integer> columns = new LinkedHashMap<>();
columns.put("column name", 10);
columns.put("another name", 20);
Header header = new Header(columns);
~~~

This will create a header with two columns and a total `width` of 30. The width is 
calculated by summing all the values in a map. 

Supplying the above header to a table would mean that the table would have width 
of 30, too.

### Adding rows to a table
Table can have any number of rows. However, each row must conform to the restriction
set by its header. Namely, it must have the same amount of columns. It also should contain
`String` keys/values only. 

Row is constructed like this:
~~~
LinkedHashMap<String, Integer> rowData = new LinkedHashMap<>();
rowData.put("column name", "cell's value");
rowData.put("another name", "some other value");
DataRow row = new DataRow(rowData);
~~~

Once we have a row, we can add it to a table:
~~~
table.addRow(rowData);
~~~

## Printing a table
Printing a table is straightforward:
~~~
Header header = ...;
Table table = new Table(header);
TablePrinter printer = TablePrinter(table);
printer.printTable();
~~~

The output will be something like this:
~~~
+-----------------------------------------------------------------------------+
| TASK | DESCRIPTION                                  | CATEGORY | DEADLINE   |
+-----------------------------------------------------------------------------+
| 27   | Example task                                 | example  | 2017-11-09 |
|      |                                              | category |            |
-------------------------------------------------------------------------------
| 28   | Example task                                 | test     | 2017-11-08 |
+-----------------------------------------------------------------------------+
~~~

If column width is too small to fit the data, the height of a row will be resized
to fit it. Other columns in the same row will be adjusted, too.

### Printed table's width
Width of the printed table is decided based on several things:
* Width of the table
* Number of columns
* Padding width

The formula is something like this:
~~~
total border width = number of columns + 1
padding width = single pad width * columns * 2
total width = table width + total border width + padding width
~~~

So if you had table with 4 columns, and `x` width, total width would be:
~~~
width = x + (4 + 1) + (1 * 4 * 2) 
width = x + 13 
~~~

You could use the above equation to figure out field sizes so that they
fit nicely into, say, 79 characters:
~~~
79 = x + 13
79 - 13 = x
x = 66
~~~

Or you could use the in-built static method:
~~~
TableDesign#usableWidth(int tableWidth, int singlePadWidth, int columns);
~~~

You can use it to decide how wide some of the header columns should be.
For example:
~~~
int usableWidth = TableDesign.usableWidth(79, 1, 4);
int first = 10;
int second = 3;
int third = 14;
int fourth = usableWidth - first - second - third;

LinkedHashMap<String, Integer> columns = new LinkedHashMap<>();
columns.put("first column", first);
columns.put("second column", second);
columns.put("third column", third);
columns.put("fourth column", fourth);
Header header = new Header(columns);
Table table = new Table(header);
~~~

This would result in a nice table that, when printed, would be 79 characters
wide.

## Changing table's design
By default, table will have:
* `+` as a corner
* `-` as a horizontal segment
* `|` as a vertical segment
* Padding of width `1` per each side.

You can modify these by creating passing a `TableDesign` object to the table:
~~~
TableDesign design = new TableDesign(table);

design.setHorizontalSegment('=');
design.setCellPaddingWidth(2);
design.setVerticalSegment('!');

TablePrinter printer = new TablePrinter(table, design);
~~~
