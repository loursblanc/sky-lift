package fr.apsprevoyance.skylift.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.apsprevoyance.skylift.constants.ErrorMessageConstants;
import fr.apsprevoyance.skylift.enums.Season;
import fr.apsprevoyance.skylift.enums.ValidationContextType;
import fr.apsprevoyance.skylift.exception.ValidationException;
import jakarta.annotation.Generated;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;

public class Sport {

    private static final Logger log = LoggerFactory.getLogger(Sport.Builder.class);

    @NotNull
    private final String id;
    @NotNull
    private final String name;
    private final String description;
    @NotNull
    private final boolean active;
    @NotNull
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
        private String id;
        private String name;
        private String description;
        private boolean active = true;
        private Season season;

        private Builder() {
        }

        public Builder id(@Nonnull String id) {
            this.id = id;
            return this;
        }

        public Builder name(@Nonnull String name) {
            this.name = name;
            return this;
        }

        public Builder description(@Nonnull String description) {
            this.description = description;
            return this;
        }

        public Builder active(@Nonnull boolean active) {
            this.active = active;
            return this;
        }

        public Builder season(@Nonnull Season season) {
            this.season = season;
            return this;
        }

        public Sport build() {

            List<String> errors = new ArrayList<>();

            if (id == null) {
                errors.add(ErrorMessageConstants.Errors.ID_NULL);
            }

            if (name == null) {
                errors.add(ErrorMessageConstants.Errors.NAME_NULL);
            }

            if (season == null) {
                errors.add(ErrorMessageConstants.Errors.SEASON_NULL);
            }

            if (!errors.isEmpty()) {

                log.warn(ErrorMessageConstants.Logs.INVALID_OBJECT_BUILD, Sport.class.getSimpleName(),
                        Sport.class.getPackage().getName(), String.join(", ", errors));

                throw new ValidationException(Sport.class.getSimpleName(), ValidationContextType.MODEL, errors);
            }

            return new Sport(this);
        }
    }

    public String getId() {
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

}
