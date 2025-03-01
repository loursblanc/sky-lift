package fr.apsprevoyance.skylift.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.apsprevoyance.skylift.constants.AnnotationMessages;
import fr.apsprevoyance.skylift.enums.SkiLiftStatus;
import fr.apsprevoyance.skylift.enums.SkiLiftType;
import fr.apsprevoyance.skylift.validation.OnCreate;
import fr.apsprevoyance.skylift.validation.OnUpdate;
import jakarta.annotation.Generated;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.groups.Default;

public class SkiLift {

    @NotNull(groups = { OnCreate.class, OnUpdate.class }, message = AnnotationMessages.Id.NULL)
    @Positive(groups = { OnCreate.class, OnUpdate.class, Default.class }, message = AnnotationMessages.Id.POSITIVE)
    private final Long id;

    @NotNull
    private final String name;

    @NotNull
    private final SkiLiftType type;

    @NotNull
    private final SkiLiftStatus status;

    private final String comment;

    @NotNull
    private final Set<String> availableSports;

    @NotNull
    private final LocalDate commissioningDate;

    @Generated("SparkTools")
    private SkiLift(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.type = builder.type;
        this.status = builder.status;
        this.comment = builder.comment;
        this.availableSports = Collections.unmodifiableSet(new HashSet<>(builder.availableSports));
        this.commissioningDate = builder.commissioningDate;
    }

    @Generated("SparkTools")
    public static Builder builder() {
        return new Builder();
    }

    @Generated("SparkTools")
    public static final class Builder {
        private static final Logger log = LoggerFactory.getLogger(Builder.class);

        private Long id;
        private String name;
        private SkiLiftType type;
        private SkiLiftStatus status;
        private String comment;
        private Set<String> availableSports = new HashSet<>();
        private LocalDate commissioningDate = LocalDate.now();

        private Builder() {
        }

        public Builder id(@Nonnull Long id) {
            this.id = id;
            return this;
        }

        public Builder name(@Nonnull String name) {
            this.name = name;
            return this;
        }

        public Builder type(@Nonnull SkiLiftType type) {
            this.type = type;
            return this;
        }

        public Builder status(@Nonnull SkiLiftStatus status) {
            this.status = status;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder availableSports(Set<String> availableSports) {
            this.availableSports = availableSports == null ? new HashSet<>() : new HashSet<>(availableSports);
            return this;
        }

        public Builder addAvailableSport(@Nonnull String sport) {
            this.availableSports.add(sport);
            return this;
        }

        public Builder commissioningDate(@Nonnull LocalDate commissioningDate) {
            this.commissioningDate = commissioningDate;
            return this;
        }

        public SkiLift build() {
            return new SkiLift(this);
        }

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public SkiLiftType getType() {
        return type;
    }

    public SkiLiftStatus getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }

    public Set<String> getAvailableSports() {
        return availableSports;
    }

    public LocalDate getCommissioningDate() {
        return commissioningDate;
    }

    @Override
    public String toString() {
        return "SkiLift{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", type=" + type + ", status=" + status
                + ", comment='" + comment + '\'' + ", availableSports=" + availableSports + ", commissioningDate="
                + commissioningDate + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(availableSports, comment, commissioningDate, id, name, status, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SkiLift other = (SkiLift) obj;
        return Objects.equals(availableSports, other.availableSports) && Objects.equals(comment, other.comment)
                && Objects.equals(commissioningDate, other.commissioningDate) && Objects.equals(id, other.id)
                && Objects.equals(name, other.name) && status == other.status && type == other.type;
    }
}