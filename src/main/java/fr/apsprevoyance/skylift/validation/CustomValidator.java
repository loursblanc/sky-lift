package fr.apsprevoyance.skylift.validation;

import java.util.List;

@FunctionalInterface
public interface CustomValidator<T> {
    void validate(T object, List<String> errors);
}
