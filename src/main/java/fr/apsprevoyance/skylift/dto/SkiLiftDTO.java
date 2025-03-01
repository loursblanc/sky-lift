package fr.apsprevoyance.skylift.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import fr.apsprevoyance.skylift.constants.AnnotationMessages;
import fr.apsprevoyance.skylift.constants.ValidationConstants;
import fr.apsprevoyance.skylift.enums.SkiLiftStatus;
import fr.apsprevoyance.skylift.enums.SkiLiftType;
import fr.apsprevoyance.skylift.validation.OnCreate;
import fr.apsprevoyance.skylift.validation.OnUpdate;
import fr.apsprevoyance.skylift.validation.ValidSkiLiftDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;

public class SkiLiftDTO {

    @NotNull(groups = OnUpdate.class, message = AnnotationMessages.Id.NULL)
    @Positive(groups = { OnUpdate.class, OnCreate.class, Default.class }, message = AnnotationMessages.Id.POSITIVE)
    private Long id;

    @NotBlank(groups = { OnUpdate.class, OnCreate.class }, message = AnnotationMessages.Name.EMPTY)
    @Size(groups = { OnUpdate.class, OnCreate.class,
            Default.class }, min = ValidationConstants.NAME_MIN_LENGTH, max = ValidationConstants.NAME_MAX_LENGTH, message = AnnotationMessages.Name.TEXT_LENGHT)
    @Pattern(groups = { OnUpdate.class, OnCreate.class,
            Default.class }, regexp = ValidationConstants.REGEX_NAME_VALID_CHARS, message = AnnotationMessages.Name.INVALID_CHARS)
    private String name;

    @NotNull(groups = { OnUpdate.class, OnCreate.class }, message = AnnotationMessages.Type.NULL)
    private SkiLiftType type;

    @NotNull(groups = { OnUpdate.class, OnCreate.class }, message = AnnotationMessages.Generic.REQUIRED)
    private SkiLiftStatus status;

    @Size(groups = { OnUpdate.class, OnCreate.class,
            Default.class }, max = ValidationConstants.DESCRIPTION_MAX_LENGTH, message = AnnotationMessages.Description.TOO_LENGHT)
    private String comment;

    @NotEmpty(groups = { OnUpdate.class, OnCreate.class }, message = AnnotationMessages.Generic.REQUIRED)
    private Set<String> availableSports = new HashSet<>();

    @NotNull(groups = { OnUpdate.class, OnCreate.class }, message = AnnotationMessages.Date.NULL)
    @ValidSkiLiftDate(groups = { OnUpdate.class, OnCreate.class, Default.class })
    private LocalDate commissioningDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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