package fr.apsprevoyance.skylift.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.apsprevoyance.skylift.constants.ErrorMessageConstants;
import fr.apsprevoyance.skylift.constants.ValidationConstants;
import fr.apsprevoyance.skylift.enums.SkiLiftStatus;
import fr.apsprevoyance.skylift.enums.SkiLiftType;
import fr.apsprevoyance.skylift.enums.ValidationContextType;
import fr.apsprevoyance.skylift.exception.ValidationException;
import jakarta.annotation.Generated;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;

public class SkiLift {

    @NotNull
    private final String id;

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

        private String id;
        private String name;
        private SkiLiftType type;
        private SkiLiftStatus status;
        private String comment;
        private Set<String> availableSports = new HashSet<>();
        private LocalDate commissioningDate = LocalDate.now();

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

        public Builder availableSports(@Nonnull Set<String> availableSports) {
            this.availableSports = new HashSet<>(availableSports);
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
            List<String> errors = collectValidationErrors();

            if (!errors.isEmpty()) {
                log.warn(ErrorMessageConstants.Logs.VALIDATION_FAILED, SkiLift.class.getSimpleName(),
                        SkiLift.class.getPackage().getName(), String.join(", ", errors));

                throw new ValidationException(SkiLift.class.getSimpleName(), ValidationContextType.MODEL, errors);
            }

            return new SkiLift(this);
        }

        private List<String> collectValidationErrors() {
            List<String> errors = new ArrayList<>();

            collectIdErrors(errors);
            collectNameErrors(errors);
            collectTypeErrors(errors);
            collectStatusErrors(errors);
            collectAvailableSportsErrors(errors);
            collectCommissioningDateErrors(errors);

            return errors;
        }

        private void collectIdErrors(List<String> errors) {
            if (id == null) {
                errors.add(ErrorMessageConstants.Errors.ID_NULL);
            } else if (id.trim().isEmpty()) {
                errors.add(ErrorMessageConstants.Errors.ID_EMPTY);
            } else if (!id.matches(ValidationConstants.REGEX_NUMERIC)) {
                errors.add(ErrorMessageConstants.Errors.ID_NOT_NUMERIC);
            }
        }

        private void collectNameErrors(List<String> errors) {
            if (name == null) {
                errors.add(ErrorMessageConstants.Errors.NAME_NULL);
            } else if (name.trim().isEmpty()) {
                errors.add(ErrorMessageConstants.Errors.NAME_EMPTY);
            } else {
                String trimmedName = name.trim();

                if (trimmedName.length() < ValidationConstants.NAME_MIN_LENGTH) {
                    errors.add(ErrorMessageConstants.Errors.NAME_TOO_SHORT);
                } else if (trimmedName.length() > ValidationConstants.NAME_MAX_LENGTH) {
                    errors.add(ErrorMessageConstants.Errors.NAME_TOO_LONG);
                }

                if (!trimmedName.matches(ValidationConstants.REGEX_NAME_VALID_CHARS)) {
                    errors.add(ErrorMessageConstants.Errors.NAME_INVALID_CHARS);
                }
            }
        }

        private void collectTypeErrors(List<String> errors) {
            if (type == null) {
                errors.add(ErrorMessageConstants.Errors.TYPE_NULL);
            }
        }

        private void collectStatusErrors(List<String> errors) {
            if (status == null) {
                errors.add(ErrorMessageConstants.Errors.STATUS_NULL);
            }
        }

        private void collectAvailableSportsErrors(List<String> errors) {
            if (availableSports == null) {
                errors.add(ErrorMessageConstants.Errors.AVAILABLE_SPORTS_NULL);
            } else if (availableSports.isEmpty()) {
                errors.add(ErrorMessageConstants.Errors.AVAILABLE_SPORTS_EMPTY);
            }
        }

        private void collectCommissioningDateErrors(List<String> errors) {
            if (commissioningDate == null) {
                errors.add(ErrorMessageConstants.Errors.COMMISSIONING_DATE_NULL);
            } else {
                if (commissioningDate.isBefore(ValidationConstants.FIRST_SKILIFT_DATE)) {
                    errors.add(ErrorMessageConstants.Errors.COMMISSIONING_DATE_TOO_OLD);
                }
            }
        }
    }

    public String getId() {
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