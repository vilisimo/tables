package exceptions;

public class EmptyCollection extends RuntimeException {

    public EmptyCollection(String message) {
        super(message);
    }
}
