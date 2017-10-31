package exceptions;

public class MismatchedColumns extends RuntimeException {

    public MismatchedColumns(String message) {
        super(message);
    }
}
