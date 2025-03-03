package fr.apsprevoyance.skylift.views;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import fr.apsprevoyance.skylift.dto.SkiLiftDTO;
import fr.apsprevoyance.skylift.dto.SportDTO;
import fr.apsprevoyance.skylift.enums.SkiLiftStatus;
import fr.apsprevoyance.skylift.enums.SkiLiftType;
import fr.apsprevoyance.skylift.model.Sport;
import fr.apsprevoyance.skylift.service.SkiLiftService;
import fr.apsprevoyance.skylift.service.SportService;

public class SkiLiftEditView extends VerticalLayout {
    private final SkiLiftService skiLiftService;
    private final SportService sportService;
    private final Binder<SkiLiftDTO> binder;
    private final Runnable onSaveCallback;
    private final Runnable onCancelCallback;
    private final SkiLiftDTO originalSkiLift;

    // Champs du formulaire
    private final TextField nameField;
    private final Select<SkiLiftType> typeSelect;
    private final Select<SkiLiftStatus> statusSelect;
    private final TextArea commentArea;
    private final MultiSelectComboBox<SportDTO> sportsComboBox;
    private final DatePicker commissioningDatePicker;

    public SkiLiftEditView(SkiLiftService skiLiftService, SportService sportService, SkiLiftDTO skiLiftToEdit,
            Runnable onSaveCallback, Runnable onCancelCallback) {
        this.skiLiftService = skiLiftService;
        this.sportService = sportService;
        this.onSaveCallback = onSaveCallback;
        this.onCancelCallback = onCancelCallback;

        // Création d'une copie de l'objet original pour éviter les références partagées
        this.originalSkiLift = copySkiLiftDTO(skiLiftToEdit);

        // Titre
        add(new H3("Modifier la remontée mécanique"));

        // Formulaire
        FormLayout formLayout = new FormLayout();

        // Initialiser les champs
        nameField = new TextField("Nom");

        typeSelect = new Select<>();
        typeSelect.setLabel("Type");
        typeSelect.setItems(SkiLiftType.values());
        typeSelect.setItemLabelGenerator(SkiLiftType::getLabel);

        statusSelect = new Select<>();
        statusSelect.setLabel("Statut");
        statusSelect.setItems(SkiLiftStatus.values());
        statusSelect.setItemLabelGenerator(SkiLiftStatus::getLabel);

        commentArea = new TextArea("Commentaire");

        sportsComboBox = new MultiSelectComboBox<>("Sports");
        List<SportDTO> allSports = sportService.findAllSports();
        sportsComboBox.setItems(allSports);
        sportsComboBox.setItemLabelGenerator(SportDTO::getName);

        commissioningDatePicker = new DatePicker("Date de mise en service");

        // Boutons
        Button saveButton = new Button("Enregistrer", e -> updateSkiLift());
        Button cancelButton = new Button("Annuler", e -> onCancelCallback.run());

        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, saveButton);
        buttonLayout.setSpacing(true);

        // Ajout des champs au formulaire
        formLayout.add(nameField, typeSelect, statusSelect, commentArea, sportsComboBox, commissioningDatePicker);

        // Binder pour tous les champs sauf sportsComboBox
        binder = new Binder<>(SkiLiftDTO.class);

        // Configuration des validations
        binder.forField(nameField).asRequired("Le nom est obligatoire").bind(SkiLiftDTO::getName, SkiLiftDTO::setName);

        binder.forField(typeSelect).asRequired("Le type est obligatoire").bind(SkiLiftDTO::getType,
                SkiLiftDTO::setType);

        binder.forField(statusSelect).asRequired("Le statut est obligatoire").bind(SkiLiftDTO::getStatus,
                SkiLiftDTO::setStatus);

        binder.forField(commentArea).bind(SkiLiftDTO::getComment, SkiLiftDTO::setComment);

        binder.forField(commissioningDatePicker).asRequired("La date de mise en service est obligatoire")
                .bind(SkiLiftDTO::getCommissioningDate, SkiLiftDTO::setCommissioningDate);

        // Préremplir les champs avec les valeurs existantes
        binder.readBean(originalSkiLift);

        // Préremplir la MultiSelectComboBox avec les sports sélectionnés
        if (originalSkiLift.getAvailableSports() != null && !originalSkiLift.getAvailableSports().isEmpty()) {
            // Créer un ensemble des IDs des sports sélectionnés
            Set<Long> selectedSportIds = originalSkiLift.getAvailableSports().stream().map(Sport::getId)
                    .collect(Collectors.toSet());

            // Sélectionner les sports correspondants dans la ComboBox
            Set<SportDTO> selectedSportDTOs = allSports.stream()
                    .filter(sportDTO -> selectedSportIds.contains(sportDTO.getId())).collect(Collectors.toSet());

            // Définir les valeurs sélectionnées
            sportsComboBox.setValue(selectedSportDTOs);
        }

        // Assemblage
        add(formLayout, buttonLayout);

        // Style
        setSizeFull();
        setPadding(true);
    }

    /**
     * Création d'une copie complète de l'objet SkiLiftDTO pour éviter les problèmes
     * de référence
     */
    private SkiLiftDTO copySkiLiftDTO(SkiLiftDTO original) {
        SkiLiftDTO copy = new SkiLiftDTO();

        copy.setId(original.getId());
        copy.setName(original.getName());
        copy.setType(original.getType());
        copy.setStatus(original.getStatus());
        copy.setComment(original.getComment());
        copy.setCommissioningDate(original.getCommissioningDate());

        // Copie profonde de la collection de sports
        if (original.getAvailableSports() != null) {
            Set<Sport> sportsCopy = original.getAvailableSports().stream()
                    .map(sport -> Sport.builder().id(sport.getId()).name(sport.getName())
                            .description(sport.getDescription()).season(sport.getSeason()).active(sport.isActive())
                            .build())
                    .collect(Collectors.toSet());
            copy.setAvailableSports(sportsCopy);
        }

        return copy;
    }

    private void updateSkiLift() {
        try {
            // Créer une nouvelle instance pour la mise à jour
            SkiLiftDTO updatedSkiLift = new SkiLiftDTO();

            // Conserver l'ID de l'objet original
            updatedSkiLift.setId(originalSkiLift.getId());

            // Utiliser le binder pour tous les champs sauf sports
            binder.writeBean(updatedSkiLift);

            // Vérification pour s'assurer que l'ID est défini
            if (updatedSkiLift.getId() == null) {
                throw new IllegalArgumentException("L'ID ne peut pas être null pour une mise à jour");
            }

            // Vérification pour s'assurer que le statut est défini
            if (updatedSkiLift.getStatus() == null) {
                updatedSkiLift.setStatus(originalSkiLift.getStatus());
            }

            // Gérer manuellement les sports sélectionnés
            Set<SportDTO> selectedSportDTOs = sportsComboBox.getValue();
            if (selectedSportDTOs == null || selectedSportDTOs.isEmpty()) {
                throw new IllegalArgumentException("Au moins un sport doit être sélectionné");
            }

            Set<Sport> sports = selectedSportDTOs.stream()
                    .map(sportDTO -> Sport.builder().id(sportDTO.getId()).name(sportDTO.getName())
                            .description(sportDTO.getDescription()).season(sportDTO.getSeason())
                            .active(sportDTO.isActive()).build())
                    .collect(Collectors.toSet());

            updatedSkiLift.setAvailableSports(sports);

            // Mise à jour de la remontée mécanique
            skiLiftService.updateSkiLift(updatedSkiLift);

            // Notification de succès
            Notification.show("Remontée mécanique modifiée avec succès", 3000, Notification.Position.MIDDLE);

            // Exécution du callback de succès
            onSaveCallback.run();
        } catch (ValidationException e) {
            Notification.show("Erreur de validation : " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("Erreur lors de la modification : " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            e.printStackTrace();
        }
    }
}