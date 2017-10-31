package table;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static utils.Validations.requireNonEmpty;

public class DataRow {

    private final LinkedHashMap<String, String> columns;

    public DataRow(LinkedHashMap<String, String> columns) {
        requireNonNull(columns, "Row columns cannot be null");
        requireNonEmpty(columns, "Row columns cannot be empty");

        this.columns = new LinkedHashMap<>(columns);
    }

    public List<String> getColumnNames() {
        return columns.entrySet().stream().map(Map.Entry::getKey).collect(toList());
    }

    public String getColumnValue(String name) {
        return columns.get(name);
    }

    public int columnCount() {
        return columns.size();
    }
}
