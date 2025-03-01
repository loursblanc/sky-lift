package fr.apsprevoyance.skylift.exception;

import fr.apsprevoyance.skylift.constants.ErrorMessageConstants;

public class EntityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String entityName;
    private final String identifier;

    public EntityNotFoundException(String entityName, String identifier) {
        super(String.format(ErrorMessageConstants.Validation.ENTITY_NOT_FOUND, entityName, identifier));
        this.entityName = entityName;
        this.identifier = identifier;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getIdentifier() {
        return identifier;
    }
}