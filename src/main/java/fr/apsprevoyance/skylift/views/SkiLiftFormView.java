package fr.apsprevoyance.skylift.views;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;

import fr.apsprevoyance.skylift.dto.SkiLiftDTO;
import fr.apsprevoyance.skylift.dto.SportDTO;
import fr.apsprevoyance.skylift.enums.SkiLiftStatus;
import fr.apsprevoyance.skylift.enums.SkiLiftType;
import fr.apsprevoyance.skylift.model.Sport;
import fr.apsprevoyance.skylift.service.SkiLiftService;
import fr.apsprevoyance.skylift.service.SportService;

@Route("ski-lifts/new")
public class SkiLiftFormView extends VerticalLayout {

    private static final long serialVersionUID = 1L;
    private final SkiLiftService skiLiftService;
    private final SportService sportService;
    private final Binder<SkiLiftDTO> binder;

    // Nouveau tableau pour afficher les sports sélectionnés
    private final Grid<SportDTO> selectedSportsGrid = new Grid<>(SportDTO.class, false);
    private final VerticalLayout selectedSportsSection = new VerticalLayout();

    public SkiLiftFormView(SkiLiftService skiLiftService, SportService sportService) {
        this.skiLiftService = skiLiftService;
        this.sportService = sportService;

        // Titre du formulaire
        H1 title = new H1("Ajouter un remonte-pente");

        // Création du formulaire
        FormLayout formLayout = new FormLayout();

        // Champs du formulaire
        TextField nameField = new TextField("Nom");
        Select<SkiLiftType> typeSelect = new Select<>();
        typeSelect.setLabel("Type");
        typeSelect.setItems(SkiLiftType.values());
        typeSelect.setItemLabelGenerator(SkiLiftType::getLabel);

        Select<SkiLiftStatus> statusSelect = new Select<>();
        statusSelect.setLabel("Statut");
        statusSelect.setItems(SkiLiftStatus.values());
        statusSelect.setItemLabelGenerator(SkiLiftStatus::getLabel);

        TextArea commentArea = new TextArea("Commentaire");

        MultiSelectComboBox<SportDTO> sportsComboBox = new MultiSelectComboBox<>("Sports");
        sportsComboBox.setItems(sportService.findAllSports());
        sportsComboBox.setItemLabelGenerator(SportDTO::getName);

        // Configuration du tableau des sports
        configureSelectedSportsGrid();

        // Ajouter un écouteur pour mettre à jour les détails des sports lorsqu'ils sont
        // sélectionnés
        sportsComboBox.addValueChangeListener(event -> {
            updateSelectedSportsGrid(event.getValue());
        });

        DatePicker commissioningDatePicker = new DatePicker("Date de mise en service");
        commissioningDatePicker.setValue(LocalDate.now());

        // Boutons
        Button saveButton = new Button("Enregistrer", e -> saveSkiLift());
        Button cancelButton = new Button("Annuler", e -> navigateToList());

        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, saveButton);
        buttonLayout.setSpacing(true);

        // Disposition des éléments
        formLayout.add(nameField, typeSelect, statusSelect, commentArea, sportsComboBox, commissioningDatePicker);

        // Configuration de la section des sports sélectionnés
        H4 sportsTableTitle = new H4("Sports sélectionnés");
        selectedSportsSection.add(sportsTableTitle, selectedSportsGrid);
        selectedSportsSection.setPadding(false);
        selectedSportsSection.setVisible(false); // Initialement caché

        // Création du binder pour la validation
        binder = new Binder<>(SkiLiftDTO.class);

        // Configuration des validations
        binder.forField(nameField).asRequired("Le nom est obligatoire")
                .withValidator(name -> name != null && name.length() >= 3, "Le nom doit faire au moins 3 caractères")
                .bind(SkiLiftDTO::getName, SkiLiftDTO::setName);

        binder.forField(typeSelect).asRequired("Le type est obligatoire").bind(SkiLiftDTO::getType,
                SkiLiftDTO::setType);

        binder.forField(statusSelect).asRequired("Le statut est obligatoire").bind(SkiLiftDTO::getStatus,
                SkiLiftDTO::setStatus);

        binder.forField(sportsComboBox).asRequired("Au moins un sport est obligatoire")
                .withValidator(sports -> !sports.isEmpty(), "Sélectionnez au moins un sport").bind(
                        // Conversion des SportDTO en Sport pour le binding
                        dto -> dto.getAvailableSports().stream().map(sport -> sportService.findSportById(sport.getId()))
                                .collect(Collectors.toSet()),
                        (dto, selectedSports) -> {
                            // Convertir les SportDTO sélectionnés en Sports
                            Set<Sport> sports = selectedSports
                                    .stream().map(sportDTO -> Sport.builder().id(sportDTO.getId())
                                            .name(sportDTO.getName()).season(sportDTO.getSeason()).build())
                                    .collect(Collectors.toSet());
                            dto.setAvailableSports(sports);
                        });

        binder.forField(commentArea).bind(SkiLiftDTO::getComment, SkiLiftDTO::setComment);

        binder.forField(commissioningDatePicker).asRequired("La date de mise en service est obligatoire").bind(
                // Getter personnalisé : retourne la date du DTO ou la date du jour
                dto -> dto.getCommissioningDate() != null ? dto.getCommissioningDate() : LocalDate.now(),
                // Setter standard
                SkiLiftDTO::setCommissioningDate);

        // Après la création du binder, initialisez le DTO avec la date du jour
        SkiLiftDTO initialDTO = new SkiLiftDTO();
        initialDTO.setCommissioningDate(LocalDate.now());
        binder.setBean(initialDTO);

        // Ajout au layout dans l'ordre demandé
        add(title, formLayout, buttonLayout, selectedSportsSection);

        // Initialisation du DTO
        binder.setBean(new SkiLiftDTO());
    }

    private void configureSelectedSportsGrid() {
        // Configuration des colonnes du tableau similaire à SkiLiftListView
        selectedSportsGrid.addColumn(SportDTO::getName).setHeader("Nom").setAutoWidth(true);
        selectedSportsGrid.addColumn(
                sport -> sport.getDescription() != null && !sport.getDescription().isEmpty() ? sport.getDescription()
                        : "-")
                .setHeader("Description").setAutoWidth(true);
        selectedSportsGrid.addColumn(sport -> sport.getSeason().getLabel()).setHeader("Saison").setAutoWidth(true);
        selectedSportsGrid.addColumn(sport -> sport.isActive() ? "Oui" : "Non").setHeader("Actif").setAutoWidth(true);

        // Définition de la hauteur du tableau
        selectedSportsGrid.setHeight("200px");
    }

    private void updateSelectedSportsGrid(Set<SportDTO> selectedSports) {
        if (selectedSports == null || selectedSports.isEmpty()) {
            selectedSportsSection.setVisible(false);
            return;
        }

        // Mise à jour des données du tableau
        selectedSportsGrid.setItems(selectedSports);
        selectedSportsSection.setVisible(true);
    }

    private void saveSkiLift() {
        try {
            SkiLiftDTO skiLiftDTO = new SkiLiftDTO();
            binder.writeBean(skiLiftDTO);

            // Sauvegarde du remonte-pente
            skiLiftService.createSkiLift(skiLiftDTO);

            // Notification de succès
            Notification.show("Remonte-pente ajouté avec succès", 3000, Notification.Position.MIDDLE);

            // Navigation vers la liste
            navigateToList();
        } catch (ValidationException e) {
            // Affichage des erreurs de validation
            Notification.show("Erreur de validation", 3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            // Gestion des autres erreurs
            Notification.show("Erreur lors de l'enregistrement : " + e.getMessage(), 3000,
                    Notification.Position.MIDDLE);
        }
    }

    private void navigateToList() {
        // Navigation vers la liste des remonte-pentes
        getUI().ifPresent(ui -> ui.navigate(SkiLiftListView.class));
    }
}