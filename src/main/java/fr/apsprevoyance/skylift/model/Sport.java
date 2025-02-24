package fr.apsprevoyance.skylift.model;

import java.util.ArrayList;
import java.util.List;

import fr.apsprevoyance.skylift.enums.Season;
import jakarta.annotation.Generated;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;

public class Sport {

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
            // TODO Gestion correct des erreurs
            if (id == null) {
                errors.add("id ne peut pas être null");
            }

            if (name == null) {
                errors.add("name ne peut pas être null");
            }

            if (season == null) {
                errors.add("season ne peut pas être null");
            }

            if (!errors.isEmpty()) {
                throw new IllegalStateException("Sport invalide: " + String.join(", ", errors));
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
