package exception;

public class DuplicateDataException
        extends BiLuxuryException {

    public DuplicateDataException(String field, String value) {

        super(field + " '" + value
                + "' đã tồn tại trong hệ thống.");
    }

    public DuplicateDataException(String message) {

        super(message);
    }
}