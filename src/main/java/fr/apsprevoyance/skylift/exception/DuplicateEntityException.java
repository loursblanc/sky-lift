package fr.apsprevoyance.skylift.exception;

public class DuplicateEntityException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String entityName;
    private final String field;
    private final String value;

    public DuplicateEntityException(String entityName, String field, String value) {
        super(String.format("Entity %s with %s %s already exists", entityName, field, value));
        this.entityName = entityName;
        this.field = field;
        this.value = value;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }
}