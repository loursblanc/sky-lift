package fr.apsprevoyance.skylift.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import fr.apsprevoyance.skylift.constants.AnnotationMessages;
import fr.apsprevoyance.skylift.constants.ValidationConstants;
import fr.apsprevoyance.skylift.enums.SkiLiftStatus;
import fr.apsprevoyance.skylift.enums.SkiLiftType;
import fr.apsprevoyance.skylift.validation.ValidSkiLiftDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SkiLiftDTO {

    @NotBlank(message = AnnotationMessages.Id.EMPTY)
    private String id;

    @NotBlank(message = AnnotationMessages.Name.EMPTY)
    @Size(min = ValidationConstants.NAME_MIN_LENGTH, max = ValidationConstants.NAME_MAX_LENGTH, message = AnnotationMessages.Name.TEXT_LENGHT)
    @Pattern(regexp = ValidationConstants.REGEX_NAME_VALID_CHARS, message = AnnotationMessages.Name.INVALID_CHARS)
    private String name;

    @NotNull(message = AnnotationMessages.Type.NULL)
    private SkiLiftType type;

    @NotNull(message = AnnotationMessages.Generic.REQUIRED)
    private SkiLiftStatus status;

    private String comment;

    @NotEmpty(message = AnnotationMessages.Generic.REQUIRED)
    private Set<String> availableSports = new HashSet<>();

    @NotNull(message = AnnotationMessages.Date.NULL)
    @ValidSkiLiftDate
    private LocalDate commissioningDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SkiLiftType getType() {
        return type;
    }

    public void setType(SkiLiftType type) {
        this.type = type;
    }

    public SkiLiftStatus getStatus() {
        return status;
    }

    public void setStatus(SkiLiftStatus status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment != null ? comment : "";
    }

    public Set<String> getAvailableSports() {
        return availableSports;
    }

    public void setAvailableSports(Set<String> availableSports) {
        this.availableSports = availableSports != null ? availableSports : new HashSet<>();
    }

    public LocalDate getCommissioningDate() {
        return commissioningDate;
    }

    public void setCommissioningDate(LocalDate commissioningDate) {
        this.commissioningDate = commissioningDate;
    }
}