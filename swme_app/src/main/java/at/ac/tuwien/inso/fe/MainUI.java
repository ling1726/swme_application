package at.ac.tuwien.inso.fe;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This is the main entry point for the vaadin user interface.
 */
@SpringUI
@Theme("valo")
public class MainUI extends UI {

    public static final String PERSONS_VIEW = "";
    public static final String PERSON_EDIT_VIEW = "PERSON_EDIT_VIEW";

    @Autowired
    private SpringViewProvider viewProvider;
    @Autowired
    private PersonsView personsView;

    private Navigator navigator;

    /**
     * This method is used for intialization tasks of the UI.
     * General UI components like page title, layout and the default view (shown on
     * application startup) are defined here.
     * @param request the http request
     */
    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("SWME Lab");

        navigator = new Navigator(this, this);
        navigator.addProvider(viewProvider);

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);

        layout.addComponent(personsView);

        setErrorHandler(new DefaultErrorHandler() {
            @Override
            public void error(com.vaadin.server.ErrorEvent event) {
                Notification.show("An unexpected error occured!", Notification.Type.ERROR_MESSAGE);
            }
        });
    }
}