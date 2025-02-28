package fr.apsprevoyance.skylift.exception;

public class EntityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String entityName;
    private final String identifier;

    public EntityNotFoundException(String entityName, String identifier) {
        super(String.format("Entity %s with id %s not found", entityName, identifier));
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