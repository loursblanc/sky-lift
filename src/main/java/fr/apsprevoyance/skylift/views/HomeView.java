package fr.apsprevoyance.skylift.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class HomeView extends VerticalLayout {

    public HomeView() {

        add(new H1("Welcome to your new application"));
        add(new Paragraph("This is the home view"));

        add(new Paragraph("You can edit this view in src\\main\\java\\fr\\apsprevoyance\\skylift\\views\\HomeView.java"));

    }
}
