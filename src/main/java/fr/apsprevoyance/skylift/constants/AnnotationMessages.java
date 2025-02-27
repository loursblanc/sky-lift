package fr.apsprevoyance.skylift.constants;

public final class AnnotationMessages {

    private AnnotationMessages() {
    }

    public static final class Id {
        public static final String EMPTY = "ID cannot be empty";
        public static final String NULL = "ID cannot be null";
        public static final String NOT_NUMERIC = "ID must contain only digits";
    }

    public static final class Name {
        public static final String EMPTY = "Name cannot be empty";
        public static final String NULL = "Name cannot be null";
        public static final String TOO_SHORT = "Name must be at least 3 characters long";
        public static final String TOO_LONG = "Name cannot exceed 50 characters";
        public static final String INVALID_CHARS = "Name can only contain letters, numbers, spaces, and apostrophes";
    }

    public static final class Season {
        public static final String NULL = "Season cannot be null";
    }

    public static final class Type {
        public static final String NULL = "Type cannot be null";
    }

    public static final class Date {
        public static final String NULL = "Date cannot be null";
        public static final String TOO_OLD = "Date is too old to be valid";
    }

    public static final class Generic {
        public static final String REQUIRED = "Field is required";
        public static final String INVALID = "Invalid field value";
    }
}