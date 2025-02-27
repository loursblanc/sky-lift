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
}