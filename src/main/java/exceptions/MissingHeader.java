package exceptions;

public class MissingHeader extends RuntimeException {

    public MissingHeader(String message) {
        super(message);
    }
}
