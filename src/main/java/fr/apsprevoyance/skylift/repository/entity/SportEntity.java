package fr.apsprevoyance.skylift.repository.entity;

import java.util.Objects;

import fr.apsprevoyance.skylift.constants.AnnotationMessages;
import fr.apsprevoyance.skylift.constants.ValidationConstants;
import fr.apsprevoyance.skylift.enums.Season;
import fr.apsprevoyance.skylift.validation.OnCreate;
import fr.apsprevoyance.skylift.validation.OnUpdate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;

@Entity
@Table(name = "sports", uniqueConstraints = { @UniqueConstraint(columnNames = "name", name = "UK_SPORT_NAME") })
public class SportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = { OnUpdate.class }, message = AnnotationMessages.Id.NULL)
    @Positive(groups = { OnUpdate.class, Default.class }, message = AnnotationMessages.Id.POSITIVE)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @NotBlank(groups = { OnUpdate.class, OnCreate.class }, message = AnnotationMessages.Name.EMPTY)
    @Size(groups = { OnCreate.class, OnUpdate.class,
            Default.class }, min = ValidationConstants.NAME_MIN_LENGTH, max = ValidationConstants.NAME_MAX_LENGTH, message = AnnotationMessages.Name.TEXT_LENGHT)
    @Pattern(groups = { OnCreate.class,
            OnUpdate.class }, regexp = ValidationConstants.REGEX_NAME_VALID_CHARS, message = AnnotationMessages.Name.INVALID_CHARS)
    private String name;

    @Lob
    @Column(name = "description", columnDefinition = "TEXT")
    @Size(max = ValidationConstants.DESCRIPTION_MAX_LENGTH, message = AnnotationMessages.Description.TOO_LENGHT)
    private String description;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "season", nullable = false)
    @NotNull(groups = { OnUpdate.class, OnCreate.class }, message = AnnotationMessages.Season.NULL)
    private Season season;

    protected SportEntity() {
    }

    public SportEntity(String name, String description, Season season, boolean active) {
        this.name = name != null ? name : "";
        this.description = description != null ? description : "";
        this.season = season;
        this.active = active;
    }

    public SportEntity(Long id, String name, String description, Season season, boolean active) {
        this(name, description, season, active);
        this.id = id;
    }

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
        this.name = name != null ? name : "";
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
        SportEntity other = (SportEntity) obj;
        return active == other.active && Objects.equals(description, other.description) && Objects.equals(id, other.id)
                && Objects.equals(name, other.name) && season == other.season;
    }
}
