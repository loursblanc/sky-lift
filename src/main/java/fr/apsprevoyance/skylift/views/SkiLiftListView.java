package fr.apsprevoyance.skylift.views;

import java.util.Set;
import java.util.stream.Collectors;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import fr.apsprevoyance.skylift.dto.SkiLiftDTO;
import fr.apsprevoyance.skylift.enums.SkiLiftStatus;
import fr.apsprevoyance.skylift.enums.SkiLiftType;
import fr.apsprevoyance.skylift.model.Sport;
import fr.apsprevoyance.skylift.service.SkiLiftService;

@Route("ski-lifts")
public class SkiLiftListView extends VerticalLayout {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final SkiLiftService skiLiftService;
    private final Grid<SkiLiftDTO> grid = new Grid<>(SkiLiftDTO.class, false);

    // Ajout des filtres
    private final ComboBox<SkiLiftType> typeFilter = new ComboBox<>("Type");
    private final ComboBox<SkiLiftStatus> statusFilter = new ComboBox<>("État");

    public SkiLiftListView(SkiLiftService skiLiftService) {
        this.skiLiftService = skiLiftService;

        // Configuration des colonnes de manière explicite
        grid.addColumn(SkiLiftDTO::getName).setHeader("Nom");
        grid.addColumn(skiLift -> skiLift.getType().getLabel()).setHeader("Type");
        grid.addColumn(skiLift -> skiLift.getStatus().getLabel()).setHeader("Statut");
        grid.addColumn(SkiLiftDTO::getComment).setHeader("Commentaire");
        grid.addColumn(SkiLiftDTO::getCommissioningDate).setHeader("Date de mise en service");

        // Colonne pour les sports disponibles avec tooltip
        grid.addComponentColumn(skiLift -> createSportsComponent(skiLift.getAvailableSports())).setHeader("Sports");

        // Colonne d'actions avec icône de suppression
        grid.addComponentColumn(this::createDeleteButton).setHeader("Actions").setFlexGrow(0).setWidth("100px")
                .setTextAlign(ColumnTextAlign.CENTER);

        // Configuration des filtres
        setupFilters();

        // Boutons d'action
        Button refreshButton = new Button("Actualiser", VaadinIcon.REFRESH.create(), e -> updateList());
        Button clearFiltersButton = new Button("Effacer les filtres", VaadinIcon.CLOSE.create(), e -> clearFilters());
        Button addButton = new Button("Ajouter un remonte-pente", VaadinIcon.PLUS.create(), e -> navigateToAddView());

        // Layout des filtres
        HorizontalLayout filterLayout = new HorizontalLayout(typeFilter, statusFilter, clearFiltersButton);
        filterLayout.setAlignItems(Alignment.BASELINE);

        // Layout des boutons
        HorizontalLayout buttonLayout = new HorizontalLayout(refreshButton, addButton);

        add(filterLayout, buttonLayout, grid);
        updateList();
    }

    private Button createDeleteButton(SkiLiftDTO skiLift) {
        Button deleteButton = new Button(new Icon(VaadinIcon.TRASH), e -> confirmDelete(skiLift));
        // deleteButton.addClassName("iconErrorTertiary");
        // ou
        deleteButton.getElement().setAttribute("theme", "icon error tertiary");
        deleteButton.getElement().setAttribute("title", "Supprimer");
        return deleteButton;
    }

    private void confirmDelete(SkiLiftDTO skiLift) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.setCloseOnEsc(true);
        confirmDialog.setCloseOnOutsideClick(false);

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.add(
                new Span("Êtes-vous sûr de vouloir supprimer la remontée mécanique \"" + skiLift.getName() + "\" ?"));
        dialogLayout.setPadding(true);

        Button confirmButton = new Button("Confirmer", e -> {
            deleteSkiLift(skiLift);
            confirmDialog.close();
        });
        confirmButton.addClassName("error");
        confirmButton.addClassName("primary");

        Button cancelButton = new Button("Annuler", e -> confirmDialog.close());

        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, confirmButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttonLayout.setWidthFull();

        dialogLayout.add(buttonLayout);
        confirmDialog.add(dialogLayout);

        confirmDialog.open();
    }

    private void deleteSkiLift(SkiLiftDTO skiLift) {
        try {
            skiLiftService.deleteSkiLift(skiLift.getId());
            updateList();
            // Vous pourriez ajouter une notification ici pour indiquer que la suppression a
            // réussi
        } catch (Exception e) {
            // Gérer l'erreur ici, par exemple avec une notification
            e.printStackTrace();
        }
    }

    private Span createSportsComponent(Set<Sport> sports) {
        if (sports == null || sports.isEmpty()) {
            return new Span("-");
        }

        // Créer le texte pour afficher les noms des sports
        String sportsText = sports.stream().map(Sport::getName).collect(Collectors.joining(", "));

        Span sportsSpan = new Span(sportsText);

        // Créer le contenu du tooltip avec les détails des sports
        StringBuilder tooltipContent = new StringBuilder();
        for (Sport sport : sports) {
            tooltipContent.append(sport.getName()).append("\n");
            tooltipContent.append("Description: ")
                    .append(sport.getDescription() != null && !sport.getDescription().isEmpty() ? sport.getDescription()
                            : "Aucune description")
                    .append("\n");
            tooltipContent.append("Saison: ").append(sport.getSeason().getLabel()).append("\n");
            tooltipContent.append("Actif: ").append(sport.isActive() ? "Oui" : "Non").append("\n\n");
        }

        // Ajouter l'attribut title qui agit comme un tooltip natif
        sportsSpan.getElement().setAttribute("title", tooltipContent.toString());

        return sportsSpan;
    }

    private void setupFilters() {
        // Configuration du filtre de type
        typeFilter.setItems(SkiLiftType.values());
        typeFilter.setItemLabelGenerator(SkiLiftType::getLabel);
        typeFilter.setClearButtonVisible(true);
        typeFilter.setPlaceholder("Tous les types");
        typeFilter.addValueChangeListener(e -> updateList());

        // Configuration du filtre de statut
        statusFilter.setItems(SkiLiftStatus.values());
        statusFilter.setItemLabelGenerator(SkiLiftStatus::getLabel);
        statusFilter.setClearButtonVisible(true);
        statusFilter.setPlaceholder("Tous les états");
        statusFilter.addValueChangeListener(e -> updateList());
    }

    private void clearFilters() {
        typeFilter.clear();
        statusFilter.clear();
        updateList();
    }

    private void updateList() {
        SkiLiftType type = typeFilter.getValue();
        SkiLiftStatus status = statusFilter.getValue();

        if (type != null || status != null) {
            grid.setItems(skiLiftService.findByTypeAndStatus(type, status));
        } else {
            grid.setItems(skiLiftService.findAllSkiLifts());
        }
    }

    private void navigateToAddView() {
        getUI().ifPresent(ui -> ui.navigate(SkiLiftFormView.class));
    }
}