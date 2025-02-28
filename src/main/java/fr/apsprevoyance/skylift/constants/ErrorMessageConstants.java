package fr.apsprevoyance.skylift.constants;

public final class ErrorMessageConstants {

    private ErrorMessageConstants() {
    }

    public static final class Validation {

        public static final String ERROR_MESSAGE = "Validation of %s failed: %s";

        public static final String FIELD_NULL = "%s cannot be null";
        public static final String FIELD_EMPTY = "%s cannot be empty";
        public static final String FIELD_MAX_LENGTH = "%s exceeds maximum length of %d";
        public static final String FIELD_MIN_LENGTH = "%s must be at least %d characters long";
        public static final String FIELD_INVALID_FORMAT = "%s has invalid format";
        public static final String FIELD_INVALID_CHARS = "%s can only contain letters (including accented characters), digits, spaces, and apostrophes";
        public static final String FIELD_NOT_NUMERIC = "%s must contain only digits";
        public static final String FIELD_TOO_OLD = "%S is too old to be valid";
        public static final String FIELD_PREDEFINED = "Cannot create a %s with predefined %s";

        public static final String PREFIX_DEFAULT = "Validation error";
        public static final String PREFIX_MODEL = "Model validation error";
        public static final String PREFIX_REQUEST = "Request validation error";
        public static final String PREFIX_BUSINESS = "Business rule error";
        public static final String PREFIX_PERSISTENCE = "Persistence error";

        public static final String REQUEST_PARSING_ERROR = "Failed to parse request body";
        public static final String JSON_PARSE_ERROR_SAFE = "Invalid JSON format at line %d, column %d";
        public static final String JSON_MAPPING_ERROR_SAFE = "Invalid data format in request";
        public static final String JSON_UNKNOWN_PROPERTY_SAFE = "Unknown field: %s";

        public static final String ENTITY_NOT_FOUND_SAFE = "The requested %s with identifier %s was not found";
        public static final String DUPLICATE_ENTITY_SAFE = "A %s with these details already exists";
    }

    public static final class Fields {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String SEASON = "season";
        public static final String TYPE = "type";
        public static final String SPORT = "sport";
        public static final String STATUS = "status";
        public static final String COMMISSIONING_DATE = "commissioningDate";
    }

    public static final class Errors {
        public static final String ID_NULL = String.format(Validation.FIELD_NULL, Fields.ID);
        public static final String ID_EMPTY = String.format(Validation.FIELD_EMPTY, Fields.ID);
        public static final String ID_NOT_NUMERIC = String.format(Validation.FIELD_NOT_NUMERIC, Fields.ID);

        public static final String NAME_NULL = String.format(Validation.FIELD_NULL, Fields.NAME);
        public static final String NAME_EMPTY = String.format(Validation.FIELD_EMPTY, Fields.NAME);
        public static final String NAME_TOO_LONG = String.format(Validation.FIELD_MAX_LENGTH, Fields.NAME,
                ValidationConstants.NAME_MAX_LENGTH);
        public static final String NAME_TOO_SHORT = String.format(Validation.FIELD_MIN_LENGTH, Fields.NAME,
                ValidationConstants.NAME_MIN_LENGTH);
        public static final String NAME_INVALID_CHARS = String.format(Validation.FIELD_INVALID_CHARS, Fields.NAME);

        public static final String SEASON_NULL = String.format(Validation.FIELD_NULL, Fields.SEASON);

        public static final String TYPE_NULL = String.format(Validation.FIELD_NULL, Fields.TYPE);;

        public static final String AVAILABLE_SPORTS_NULL = String.format(Validation.FIELD_NULL, Fields.SPORT);;
        public static final String AVAILABLE_SPORTS_EMPTY = String.format(Validation.FIELD_EMPTY, Fields.SPORT);;

        public static final String COMMISSIONING_DATE_NULL = String.format(Validation.FIELD_NULL,
                Fields.COMMISSIONING_DATE);
        public static final String COMMISSIONING_DATE_TOO_OLD = String.format(Validation.FIELD_TOO_OLD,
                Fields.COMMISSIONING_DATE);

        public static final String STATUS_NULL = String.format(Validation.FIELD_NULL, Fields.STATUS);

        public static final String SPORT_NULL = String.format(Validation.FIELD_NULL, Fields.SPORT);
        public static final String SPORT_ID_PREDEFINED = String.format(Validation.FIELD_PREDEFINED, Fields.SPORT,
                Fields.ID);

    }

    public static final class Formats {
        public static final String ERROR_WITH_MODEL = "%s for %s";
        public static final String FIELD_ERROR = "%s: %s";
    }

    public static final class General {
        public static final String INTERNAL_ERROR = "An internal error occurred";
    }

    public static final class Logs {
        public static final String VALIDATION_FAILED = "Validation failed for {} (context: {}): {}";
        public static final String METHOD_ARGS_VALIDATION_FAILED = "Method argument validation failed: {}";
        public static final String UNCAUGHT_EXCEPTION = "Uncaught exception: ";
        public static final String INVALID_OBJECT_BUILD = "Attempting to build invalid {} object in package {}: {}";
        public static final String REQUEST_PARSING_FAILED = "Request parsing failed: {}";
        public static final String ENTITY_NOT_FOUND = "Entity {} with identifier {} not found";
        public static final String DUPLICATE_ENTITY = "Duplicate entity {} with {}={}";
    }
}