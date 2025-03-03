package fr.apsprevoyance.skylift.views;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
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

    // Section pour les cartes des sports
    private final VerticalLayout selectedSportsSection = new VerticalLayout();
    private final FlexLayout sportsCardsLayout = new FlexLayout();

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

        // Configuration de la section des cartes de sports
        configureSportsCardsSection();

        // Ajouter un écouteur pour mettre à jour les cartes des sports lorsqu'ils sont
        // sélectionnés
        sportsComboBox.addValueChangeListener(event -> {
            updateSportsCards(event.getValue());
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

    private void configureSportsCardsSection() {
        H4 sectionTitle = new H4("Sports sélectionnés");

        // Configuration du layout des cartes
        sportsCardsLayout.setWidthFull();
        sportsCardsLayout.getStyle().set("flex-wrap", "wrap").set("gap", "16px").set("justify-content", "flex-start");

        selectedSportsSection.add(sectionTitle, sportsCardsLayout);
        selectedSportsSection.setVisible(false); // Initialement caché
        selectedSportsSection.setPadding(true);
        selectedSportsSection.setSpacing(true);
    }

    private void updateSportsCards(Set<SportDTO> selectedSports) {
        sportsCardsLayout.removeAll();

        if (selectedSports == null || selectedSports.isEmpty()) {
            selectedSportsSection.setVisible(false);
            return;
        }

        for (SportDTO sport : selectedSports) {
            sportsCardsLayout.add(createSportCard(sport));
        }

        selectedSportsSection.setVisible(true);
    }

    private Div createSportCard(SportDTO sport) {
        // Création de la carte
        Div card = new Div();
        card.getStyle().set("background-color", "white").set("border-radius", "8px")
                .set("box-shadow", "0 2px 6px rgba(0,0,0,0.1)").set("overflow", "hidden").set("width", "240px")
                .set("min-height", "200px").set("display", "flex").set("flex-direction", "column");

        // Image ou icône représentant le sport
        Image sportImage = createSportImage(sport);
        sportImage.setWidth("100%");
        sportImage.setHeight("120px");

        // Contenu de la carte
        Div cardContent = new Div();
        cardContent.getStyle().set("padding", "16px").set("display", "flex").set("flex-direction", "column").set("flex",
                "1");

        // Titre de la carte (nom du sport)
        H4 sportName = new H4(sport.getName());
        sportName.getStyle().set("margin-top", "0").set("margin-bottom", "8px");

        // Description du sport
        Paragraph description = new Paragraph(
                sport.getDescription() != null && !sport.getDescription().isEmpty() ? sport.getDescription()
                        : "Aucune description disponible");
        description.getStyle().set("margin", "0 0 8px 0").set("font-size", "14px").set("color",
                "var(--lumo-secondary-text-color)");

        // Informations supplémentaires
        Div infoSection = new Div();
        infoSection.getStyle().set("margin-top", "auto").set("font-size", "13px");

        // Saison
        Span seasonLabel = new Span("Saison: ");
        seasonLabel.getStyle().set("font-weight", "bold");
        Span seasonValue = new Span(sport.getSeason().getLabel());
        Div seasonInfo = new Div(seasonLabel, seasonValue);

        // Statut actif
        Span activeLabel = new Span("Actif: ");
        activeLabel.getStyle().set("font-weight", "bold");
        Span activeValue = new Span(sport.isActive() ? "Oui" : "Non");
        Div activeInfo = new Div(activeLabel, activeValue);

        infoSection.add(seasonInfo, activeInfo);
        cardContent.add(sportName, description, infoSection);
        card.add(sportImage, cardContent);

        return card;
    }

    private Image createSportImage(SportDTO sport) {
        // Dans une application réelle, vous chargeriez une image depuis une base de
        // données
        // ou un service de fichiers. Ici, nous utilisons des placeholders basés sur le
        // nom du sport.
        String imagePath;
        String sportName = sport.getName().toLowerCase().trim();
        // Sélectionner une image en fonction du nom du sport String sportName =

        // Utilisation d'un switch avec ==
        switch (sportName) {
        case "ski":
            imagePath = "/images/ski.jpg";
            break;
        case "snowboard":
        case "surf":
            imagePath = "/images/snowboard.jpg";
            break;
        case "snowscoot":
            imagePath = "/images/snowscoot.jpg";
            break;
        case "luge":
            imagePath = "/images/sledge.jpg";
            break;
        default:
            imagePath = "/images/placeholder.jpg";
            break;
        }

        if (sportName.contains("ski")) {
            imagePath = "/images/ski.jpg";
        } else if (sportName.contains("surf") || sportName.contains("snowboard")) {
            imagePath = "/images/snowboard.jpg";
        } else if (sportName.contains("snowscoot")) {
            imagePath = "/images/snowscoot.jpg";
        } else if (sportName.contains("luge")) {
            imagePath = "/images/sledge.jpg";
        }

        // Pour l'exemple, nous utilisons un placeholder générique puisque nous n'avons
        // pas d'images réelles
        // Dans une application réelle, vous utiliseriez le chemin d'image correct
        Image image = new Image(imagePath, sport.getName());

        // Comme alternative, nous pouvons utiliser une icône représentative
        // pour démontrer le concept sans les vrais fichiers image
        Div iconContainer = new Div();
        iconContainer.getStyle().set("width", "100%").set("height", "120px").set("display", "flex")
                .set("align-items", "center").set("justify-content", "center")
                .set("background-color", "var(--lumo-contrast-10pct)");
        /*
         * Icon sportIcon = getSportIcon(sport);
         * System.out.println(sportIcon.getClassName()); sportIcon.setSize("48px");
         * sportIcon.setColor("var(--lumo-primary-color)");
         * iconContainer.add(sportIcon);
         */
        return image;
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