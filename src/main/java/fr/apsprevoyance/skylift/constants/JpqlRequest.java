package fr.apsprevoyance.skylift.constants;

public final class JpqlRequest {

    private JpqlRequest() {
    }

    public static String SPORT_COUNT_BY_NAME = "SELECT COUNT(s) FROM SportEntity s WHERE LOWER(s.name) = LOWER(:name)";

}