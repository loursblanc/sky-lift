package fr.apsprevoyance.skylift.repository.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import fr.apsprevoyance.skylift.constants.AnnotationMessages;
import fr.apsprevoyance.skylift.constants.ValidationConstants;
import fr.apsprevoyance.skylift.enums.SkiLiftStatus;
import fr.apsprevoyance.skylift.enums.SkiLiftType;
import fr.apsprevoyance.skylift.validation.OnCreate;
import fr.apsprevoyance.skylift.validation.OnUpdate;
import fr.apsprevoyance.skylift.validation.ValidSkiLiftDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;

@Entity
@Table(name = "ski_lifts")
public class SkiLiftEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = { OnUpdate.class }, message = AnnotationMessages.Id.NULL)
    @Positive(groups = { OnUpdate.class, Default.class }, message = AnnotationMessages.Id.POSITIVE)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank(groups = { OnUpdate.class, OnCreate.class }, message = AnnotationMessages.Name.EMPTY)
    @Size(groups = { OnCreate.class, OnUpdate.class,
            Default.class }, min = ValidationConstants.NAME_MIN_LENGTH, max = ValidationConstants.NAME_MAX_LENGTH, message = AnnotationMessages.Name.TEXT_LENGHT)
    @Pattern(groups = { OnCreate.class,
            OnUpdate.class }, regexp = ValidationConstants.REGEX_NAME_VALID_CHARS, message = AnnotationMessages.Name.INVALID_CHARS)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @NotNull(groups = { OnUpdate.class, OnCreate.class }, message = AnnotationMessages.Type.NULL)
    private SkiLiftType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotNull(groups = { OnUpdate.class, OnCreate.class }, message = AnnotationMessages.Status.NULL)
    private SkiLiftStatus status;

    @Lob
    @Column(name = "comment", columnDefinition = "TEXT")
    @Size(max = ValidationConstants.DESCRIPTION_MAX_LENGTH, message = AnnotationMessages.Comment.TOO_LENGHT)
    private String comment;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "SkiLift_Sport", joinColumns = @JoinColumn(name = "ski_lift_id", foreignKey = @ForeignKey(name = "SKILIFT_SPORT_FK_SKI_LIFTS")), inverseJoinColumns = @JoinColumn(name = "sport_id", foreignKey = @ForeignKey(name = "SKILIFT_SPORT_FK_SPORTS_FK_SPORTS")))
    @NotEmpty(groups = { OnUpdate.class, OnCreate.class }, message = AnnotationMessages.AvaiableSports.EMPTY)
    private Set<SportEntity> availableSports = new HashSet<>();

    @Column(name = "commissioning_date", nullable = false)
    @NotNull(groups = { OnUpdate.class, OnCreate.class }, message = AnnotationMessages.Date.NULL)
    @ValidSkiLiftDate(groups = { OnUpdate.class, OnCreate.class, Default.class })
    private LocalDate commissioningDate;

    protected SkiLiftEntity() {
    }

    public SkiLiftEntity(String name, SkiLiftType type, SkiLiftStatus status, String comment,
            Set<SportEntity> availableSports, LocalDate commissioningDate) {
        this.name = name != null ? name : "";
        this.type = type;
        this.status = status;
        this.comment = comment != null ? comment : "";
        setAvailableSports(availableSports);
        this.commissioningDate = commissioningDate;
    }

    public SkiLiftEntity(Long id, String name, SkiLiftType type, SkiLiftStatus status, String comment,
            Set<SportEntity> availableSports, LocalDate commissioningDate) {
        this(name, type, status, comment, availableSports, commissioningDate);
        this.id = id;
    }

    // Getters et setters
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

    public Set<SportEntity> getAvailableSports() {
        return availableSports;
    }

    public void setAvailableSports(Set<SportEntity> availableSports) {
        this.availableSports = new HashSet<>();
        if (availableSports != null) {
            this.availableSports.addAll(availableSports);
        }
    }

    public LocalDate getCommissioningDate() {
        return commissioningDate;
    }

    public void setCommissioningDate(LocalDate commissioningDate) {
        this.commissioningDate = commissioningDate;
    }

    @Override
    public String toString() {
        return "SkiLiftEntity{" + "id=" + id + ", name='" + name + '\'' + ", type=" + type + ", status=" + status
                + ", comment='" + comment + '\'' + ", availableSports=" + availableSports + ", commissioningDate="
                + commissioningDate + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SkiLiftEntity that = (SkiLiftEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && type == that.type
                && status == that.status && Objects.equals(comment, that.comment)
                && Objects.equals(availableSports, that.availableSports)
                && Objects.equals(commissioningDate, that.commissioningDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, status, comment, availableSports, commissioningDate);
    }
}