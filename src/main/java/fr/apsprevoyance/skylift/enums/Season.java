package fr.apsprevoyance.skylift.enums;

public enum Season {

    WINTER("Hiver"), SUMMER("Été"), BOTH("Toutes saisons");

    private final String label;

    Season(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}