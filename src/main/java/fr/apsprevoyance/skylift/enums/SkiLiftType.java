package fr.apsprevoyance.skylift.enums;

import fr.apsprevoyance.skylift.constants.SkiLiftTypeLabels;

public enum SkiLiftType {

    TELESIEGE(SkiLiftTypeLabels.TELESIEGE),
    TELESKI(SkiLiftTypeLabels.TELESKI),
    TELECABLE(SkiLiftTypeLabels.TELECABLE),
    FENICULAR(SkiLiftTypeLabels.FENICULAR);

    private final String label;

    SkiLiftType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
