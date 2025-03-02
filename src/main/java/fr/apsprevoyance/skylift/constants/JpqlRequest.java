package fr.apsprevoyance.skylift.constants;

public final class JpqlRequest {

    private JpqlRequest() {
    }

    public static final class Sport {
        public static String COUNT_BY_NAME = "SELECT COUNT(s) FROM SportEntity s WHERE LOWER(s.name) = LOWER(:name)";
        public static final String FIND_ALL = "SELECT s FROM SportEntity s";
        public static final String FIND_BY_ID = "SELECT s FROM SportEntity s WHERE s.id = :id";
        public static final String COUNT_BY_ID = "SELECT COUNT(s) FROM SportEntity s WHERE s.id = :id";
        public static final String COUNT_BY_NAME_EXCLUDING_CURRENT = "SELECT COUNT(s) FROM SportEntity s WHERE LOWER(s.name) = LOWER(:name) AND s.id != :id";
    }

    public static final class SkiLift {
        public static final String COUNT_BY_NAME = "SELECT COUNT(s) FROM SkiLiftEntity s WHERE LOWER(s.name) = LOWER(:name)";
        public static final String FIND_ALL = "SELECT s FROM SkiLiftEntity s";
        public static final String FIND_BY_ID = "SELECT s FROM SkiLiftEntity s WHERE s.id = :id";
        public static final String COUNT_BY_ID = "SELECT COUNT(s) FROM SkiLiftEntity s WHERE s.id = :id";
        public static final String COUNT_BY_NAME_EXCLUDING_CURRENT = "SELECT COUNT(s) FROM SkiLiftEntity s WHERE LOWER(s.name) = LOWER(:name) AND s.id != :id";
    }

}