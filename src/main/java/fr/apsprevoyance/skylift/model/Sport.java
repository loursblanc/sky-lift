package fr.apsprevoyance.skylift.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.apsprevoyance.skylift.constants.AnnotationMessages;
import fr.apsprevoyance.skylift.constants.ErrorMessageConstants;
import fr.apsprevoyance.skylift.constants.ValidationConstants;
import fr.apsprevoyance.skylift.enums.Season;
import fr.apsprevoyance.skylift.enums.ValidationContextType;
import fr.apsprevoyance.skylift.exception.ValidationException;
import jakarta.annotation.Generated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class Sport {
    @NotNull(message = AnnotationMessages.Id.NULL)
    @Positive(message = AnnotationMessages.Id.POSITIVE)
    private final Long id;

    @NotBlank(message = AnnotationMessages.Name.EMPTY)
    @Size(min = ValidationConstants.NAME_MIN_LENGTH, max = ValidationConstants.NAME_MAX_LENGTH, message = AnnotationMessages.Name.TEXT_LENGHT)
    @Pattern(regexp = ValidationConstants.REGEX_NAME_VALID_CHARS, message = AnnotationMessages.Name.INVALID_CHARS)
    private final String name;
    private final String description;
    private final boolean active;

    @NotNull(message = AnnotationMessages.Season.NULL)
    private final Season season;

    @Generated("SparkTools")
    private Sport(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.active = builder.active;
        this.season = builder.season;
    }

    @Generated("SparkTools")
    public static Builder builder() {
        return new Builder();
    }

    @Generated("SparkTools")
    public static final class Builder {

        private static final Logger log = LoggerFactory.getLogger(Sport.Builder.class);

        private Long id;
        private String name;
        private String description;
        private boolean active = true;
        private Season season;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description != null ? description : "";
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Builder season(Season season) {
            this.season = season;
            return this;
        }

        public Sport build() {

            List<String> errors = collectValidationErrors();

            if (!errors.isEmpty()) {
                log.warn(ErrorMessageConstants.Logs.VALIDATION_FAILED, SkiLift.class.getSimpleName(),
                        SkiLift.class.getPackage().getName(), String.join(", ", errors));

                throw new ValidationException(SkiLift.class.getSimpleName(), ValidationContextType.MODEL, errors);
            }

            return new Sport(this);
        }

        private List<String> collectValidationErrors() {

            List<String> errors = new ArrayList<String>();

            if (id == null) {
                errors.add(ErrorMessageConstants.Errors.ID_NULL);
            }

            if (name == null) {
                errors.add(ErrorMessageConstants.Errors.NAME_NULL);
            }

            if (season == null) {
                errors.add(ErrorMessageConstants.Errors.SEASON_NULL);
            }
            return errors;
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public Season getSeason() {
        return season;
    }

    @Override
    public String toString() {
        return "Sport{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", description='" + description + '\''
                + ", active=" + active + ", season=" + season + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(active, description, id, name, season);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Sport other = (Sport) obj;
        return active == other.active && Objects.equals(description, other.description) && Objects.equals(id, other.id)
                && Objects.equals(name, other.name) && season == other.season;
    }
}