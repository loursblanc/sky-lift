package fr.apsprevoyance.skylift.constants;

import fr.apsprevoyance.skylift.enums.Season;

public final class TestConstants {

    private TestConstants() {
    }

    public static final class Sport {
        public static final String VALID_ID = "123";
        public static final String VALID_NAME = "Ski";
        public static final String VALID_DESCRIPTION = "alpine skiing ";
        public static final boolean VALID_ACTIVE = true;
        public static final Season VALID_SEASON = Season.WINTER;

        public static final String DIFFERENT_ID = "456";
        public static final String DIFFERENT_NAME = "Snowboard";
        public static final String DIFFERENT_DESCRIPTION = "Snowboard freestyle";

        public static final String INVALID_ID_NON_NUMERIC = "abc";

    }
}