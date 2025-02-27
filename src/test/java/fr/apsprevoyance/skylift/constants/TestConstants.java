package fr.apsprevoyance.skylift.constants;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import fr.apsprevoyance.skylift.enums.AllowedSportType;
import fr.apsprevoyance.skylift.enums.Season;
import fr.apsprevoyance.skylift.enums.SkiLiftStatus;
import fr.apsprevoyance.skylift.enums.SkiLiftType;

public final class TestConstants {

    private TestConstants() {
    }

    public static final String SPORT_VALID_ID = "111";
    public static final String SPORT_VALID_DESCRIPTION = "Alpine skiing";
    public static final String SPORT_DIFFERENT_ID = "456";
    public static final String SPORT_DIFFERENT_DESCRIPTION = "Snowboard freestyle";

    public static final String SKILIFT_VALID_ID = "789";
    public static final String SKILIFT_VALID_NAME = "Télésiège des Marmottes";
    public static final String SKILIFT_VALID_COMMENT = "Main ski lift";
    public static final String SKILIFT_DIFFERENT_ID = "101112";
    public static final String SKILIFT_DIFFERENT_NAME = "Télécabine de la Combe";;
    public static final String SKILIFT_DIFFERENT_COMMENT = "Closed for maintenance";;

    public static final class Sport {
        public static final String VALID_ID = SPORT_VALID_ID;
        public static final String VALID_NAME = AllowedSportType.SKI.getLabel();
        public static final String VALID_DESCRIPTION = SPORT_VALID_DESCRIPTION;
        public static final boolean VALID_ACTIVE = true;
        public static final Season VALID_SEASON = Season.WINTER;

        public static final String DIFFERENT_ID = SPORT_DIFFERENT_ID;
        public static final String DIFFERENT_NAME = AllowedSportType.SNOWBOARD.getLabel();
        public static final String DIFFERENT_DESCRIPTION = SPORT_DIFFERENT_DESCRIPTION;

        public static final String INVALID_ID_NON_NUMERIC = "abc";

        private Sport() {
        }
    }

    public static final class SkiLift {
        public static final String VALID_ID = SKILIFT_VALID_ID;
        public static final String VALID_NAME = SKILIFT_VALID_NAME;
        public static final SkiLiftType VALID_TYPE = SkiLiftType.TELESIEGE;
        public static final SkiLiftStatus VALID_STATUS = SkiLiftStatus.OPEN;
        public static final String VALID_COMMENT = SKILIFT_VALID_COMMENT;
        public static final LocalDate VALID_COMMISSIONING_DATE = LocalDate.now();
        public static final Set<String> VALID_AVAILABLE_SPORTS = createValidSports();

        public static final String DIFFERENT_ID = SKILIFT_DIFFERENT_ID;
        public static final String DIFFERENT_NAME = SKILIFT_DIFFERENT_NAME;
        public static final SkiLiftType DIFFERENT_TYPE = SkiLiftType.TELECABLE;
        public static final SkiLiftStatus DIFFERENT_STATUS = SkiLiftStatus.CLOSED;
        public static final String DIFFERENT_COMMENT = SKILIFT_DIFFERENT_COMMENT;
        public static final LocalDate DIFFERENT_COMMISSIONING_DATE = LocalDate.now().minusYears(5);
        public static final Set<String> DIFFERENT_AVAILABLE_SPORTS = createDifferentSports();

        public static final String INVALID_ID_NON_NUMERIC = "abc";
        public static final LocalDate INVALID_DATE_TOO_OLD = LocalDate.of(1900, 1, 1);

        private static Set<String> createValidSports() {
            Set<String> sports = new HashSet<>();
            sports.add(AllowedSportType.SKI.getLabel());
            sports.add(AllowedSportType.SNOWBOARD.getLabel());
            return sports;
        }

        private static Set<String> createDifferentSports() {
            Set<String> sports = new HashSet<>();
            sports.add(AllowedSportType.SKI.getLabel());
            sports.add(AllowedSportType.SLEDGE.getLabel());
            return sports;
        }

        private SkiLift() {
        }
    }

    public static final class ValidationTestMessages {

        public static final String EMPTY_ERROR_LIST_FOR_VALID = "Error list should be empty for a valid object";
        public static final String ERROR_LIST_NOT_EMPTY = "Error list should not be empty";

        public static final String ERROR_FOR_ID = "There should be an error for id";
        public static final String ERROR_FOR_NAME = "There should be an error for name";
        public static final String ERROR_FOR_DATE = "There should be an error for date";

        public static final String ONE_ERROR_IN_EXCEPTION = "There should be 1 error in the exception";
        public static final String TWO_ERROR_IN_EXCEPTION = "There should be 2 error in the exception";
        public static final String THREE_ERROR_IN_EXCEPTION = "There should be 3 verror in the exception";
        public static final String FOUR_ERRORS_IN_EXCEPTION = "There should be 4 errors in the exception";

        public static final String NO_EXCEPTION_FOR_VALID = "No exception should be thrown for a valid object";
        public static final String EXCEPTION_FOR_INVALID = "A ValidationException should be thrown for an invalid object";
        public static final String NO_EXCEPTION_ALL_VALIDATIONS_PASS = "No exception should be thrown when all validations pass";
        public static final String EXCEPTION_CUSTOM_VALIDATION_FAILS = "A ValidationException should be thrown when custom validation fails";
        public static final String EXCEPTION_BOTH_VALIDATIONS_FAIL = "A ValidationException should be thrown when both validations fail";

        public static final String ENTITY_NAME_CORRECT = "Entity name should be correct";
        public static final String CONTEXT_TYPE_MODEL = "Context type should be MODEL";
        public static final String CUSTOM_ERROR_IN_EXCEPTION = "The custom error message should be in the exception";
        public static final String DATE_TOO_OLD_ERROR_PRESENT = "The error about the date being too old should be present";

        public static final String ID_CANNOT_BE_EMPTY = "ID cannot be empty";
        public static final String ID_MUST_BE_NUMERIC = "ID must be numeric";
        public static final String NAME_CANNOT_BE_EMPTY = "Name cannot be empty";
        public static final String NAME_MIN_LENGTH = "Name must contain at least 3 characters";
        public static final String DATE_CANNOT_BE_NULL = "Date cannot be null";

        private ValidationTestMessages() {
        }
    }
}