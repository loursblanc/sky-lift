package fr.apsprevoyance.skylift.enums;

import fr.apsprevoyance.skylift.constants.SportLabels;

public enum AllowedSportType {

    SKI(SportLabels.SKI), SURF(SportLabels.SNOWBOARD), SNOWSCOOT(SportLabels.SNOWSCOOT), LUGE(SportLabels.SLEDGE);

    private final String label;

    AllowedSportType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
