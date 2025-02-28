package fr.apsprevoyance.skylift.constants;

import java.time.LocalDate;

public final class ValidationConstants {

    private ValidationConstants() {
    }

    public static final int NAME_MIN_LENGTH = 3;
    public static final int NAME_MAX_LENGTH = 50;
    public static final int DESCRIPTION_MAX_LENGTH = 1000;

    public static final LocalDate FIRST_SKILIFT_DATE = LocalDate.of(1934, 12, 23);

    public static final String REGEX_NAME_VALID_CHARS = "[a-zA-ZÀ-ÿ0-9\\s']+";
    public static final String REGEX_NUMERIC = "\\d+";

}
