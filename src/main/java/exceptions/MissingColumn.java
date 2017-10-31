package exceptions;

public class MissingColumn extends RuntimeException {

    public MissingColumn(String message) {
        super(message);
    }
}
