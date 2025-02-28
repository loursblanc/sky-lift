package fr.apsprevoyance.skylift.dto;

import fr.apsprevoyance.skylift.constants.AnnotationMessages;
import fr.apsprevoyance.skylift.constants.ValidationConstants;
import fr.apsprevoyance.skylift.enums.Season;
import fr.apsprevoyance.skylift.validation.OnCreate;
import fr.apsprevoyance.skylift.validation.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class SportDTO {

    @NotNull(groups = OnUpdate.class, message = AnnotationMessages.Id.NULL)
    @Positive(message = AnnotationMessages.Id.POSITIVE)
    private Long id;

    @NotBlank(groups = { OnUpdate.class, OnCreate.class }, message = AnnotationMessages.Name.EMPTY)
    @Size(min = ValidationConstants.NAME_MIN_LENGTH, max = ValidationConstants.NAME_MAX_LENGTH, message = AnnotationMessages.Name.TEXT_LENGHT)
    @Pattern(regexp = ValidationConstants.REGEX_NAME_VALID_CHARS, message = AnnotationMessages.Name.INVALID_CHARS)
    private String name;

    @Size(max = ValidationConstants.DESCRIPTION_MAX_LENGTH, message = AnnotationMessages.Description.TOO_LENGHT)
    private String description;

    private boolean active = true;

    @NotNull(groups = { OnUpdate.class, OnCreate.class }, message = AnnotationMessages.Season.NULL)
    private Season season;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description != null ? description : "";
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }
}