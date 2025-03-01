package fr.apsprevoyance.skylift.enums;

import fr.apsprevoyance.skylift.constants.SkiLiftStatusLabels;

public enum SkiLiftStatus {

    OPEN(SkiLiftStatusLabels.OPEN), CLOSED(SkiLiftStatusLabels.CLOSED), MAINTENANCE(SkiLiftStatusLabels.MAINTENANCE);

    private final String label;

    SkiLiftStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
