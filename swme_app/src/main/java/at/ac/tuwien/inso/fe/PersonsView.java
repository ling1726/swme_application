package at.ac.tuwien.inso.fe;

import at.ac.tuwien.inso.entities.Address;
import at.ac.tuwien.inso.entities.Country;
import at.ac.tuwien.inso.entities.Person;
import at.ac.tuwien.inso.entities.Title;
import at.ac.tuwien.inso.services.CountryServiceImpl;
import at.ac.tuwien.inso.services.PersonServiceImpl;
import at.ac.tuwien.inso.services.TitleServiceImpl;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * This view shows a list of all persons including their personal details.
 * This is the main user interface view.
 */
@UIScope
@SpringView(name = MainUI.PERSONS_VIEW)
public class PersonsView extends VerticalLayout implements View {
    @Autowired
    private PersonServiceImpl personService;
    @Autowired
    private TitleServiceImpl titleService;
    @Autowired
    private CountryServiceImpl countryService;


    private Table table;
    private Button btnInsert;
    private Button btnFilter;
    private Button btnReset;

    private TextField tfFirstName;
    private TextField tfFamilyName;
    private TextField tfAddress;
    private TextField tfMail;
    private TextField tfAge;
    private ComboBox cbTitle;
    private TextField tfPersonName;
    private ComboBox cbGender;
    private DateField dfBirth;

    private TextField tfStreet;
    private TextField tfDoorNumber;
    private TextField tfStreetNumber;
    private ComboBox cbCountry;
    private TextField tfProvince;
    private TextField tfPostCode;
    private TextField tfCity;

    /**
     * Initialization for the UI components of this view.
     * Creates a table to view all persons and their details.
     * If an entry is double-clicked or the 'Insert' button is clicked, the PersonEditView is called
     * with the corresponding parameter for update or insert.
     */
    @PostConstruct
    public void init(){
        this.setMargin(true);
        this.setSpacing(true);

        table = new Table() {
            @Override
            protected String formatPropertyValue(Object rowId, Object colId, Property<?> property) {
                Object value = property.getValue();

                if (value instanceof Date) {
                    Date dateValue = (Date) value;

                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    return df.format(dateValue);
                }

                return super.formatPropertyValue(rowId, colId, property);
            }
        };

        table.setPageLength(10);
        table.setSizeFull();

        table.addContainerProperty("personId", Long.class, null, "Id", null, null);
        table.addContainerProperty("familyName", String.class, null, "Family Name", null, null);
        table.addContainerProperty("firstName", String.class, null, "First Name", null, null);
        table.addContainerProperty("personName", String.class, null, "Person Name", null, null);
        table.addContainerProperty("address", String.class, null, "Address", null, null);
        table.addContainerProperty("gender", String.class, null, "Gender", null, null);
        table.addContainerProperty("mail", String.class, null, "Mail", null, null);
        table.addContainerProperty("age", String.class, null, "Age", null, null);
        table.addContainerProperty("title", String.class, null, "Title", null, null);
        table.addContainerProperty("birth", Date.class, null, "Birth", null, null);

        table.setVisibleColumns(new Object[]{"familyName", "firstName", "personName", "address", "gender", "mail", "age", "title", "birth"});

        loadPersons();

        addComponent(getFilterLayout());
        addComponent(getAddressLayout());
        addComponent(table);

        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                if (itemClickEvent.isDoubleClick()) {
                    Long personId = (Long) itemClickEvent.getItem().getItemProperty("personId").getValue();

                    UI.getCurrent().getNavigator().navigateTo(MainUI.PERSON_EDIT_VIEW + "/" + personId);
                }
            }
        });

        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.setSpacing(true);

        btnInsert = new Button("Insert");
        btnInsert.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                UI.getCurrent().getNavigator().navigateTo(MainUI.PERSON_EDIT_VIEW);
            }
        });

        btnReset = new Button("Reset");
        btnReset.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                loadPersons();
                clearFilters();
            }
        });

        btnLayout.addComponent(btnInsert);
        btnLayout.addComponent(btnFilter);
        btnLayout.addComponent(btnReset);

        addComponent(btnLayout);
    }

    private void clearFilters() {
        tfFirstName.clear();
        tfFamilyName.clear();
        tfMail.clear();
        tfAge.clear();
        cbTitle.clear();
        tfPersonName.clear();
        cbGender.clear();
        dfBirth.clear();

        tfStreetNumber.clear();
        tfStreet.clear();
        tfCity.clear();
        tfDoorNumber.clear();
        tfPostCode.clear();
        tfProvince.clear();
        cbCountry.clear();
    }

    private void loadTitles(){
        List<Title> titles = titleService.getTitles();

        cbTitle.addItems(titles);
        cbTitle.setItemCaptionMode(AbstractSelect.ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
    }

    private void loadCountries(){
        List<Country> countries = countryService.getCountries();
        cbCountry.addItems(countries);
        cbCountry.setItemCaptionMode(AbstractSelect.ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
    }

    private void loadPersons() {
        List<Person> persons = personService.getPersons();

        addPersons(persons);
    }

    private void addPersons(List<Person> persons) {
        table.removeAllItems();

        for (Person p : persons) {
            Object rowItemId = table.addItem();

            Item rowItem = table.getItem(rowItemId);
            rowItem.getItemProperty("personId").setValue(p.getId());
            rowItem.getItemProperty("firstName").setValue(p.getFirstName());
            rowItem.getItemProperty("familyName").setValue(p.getFamilyName());
            rowItem.getItemProperty("personName").setValue(p.getPersonName());
            rowItem.getItemProperty("address").setValue(p.getAddress().toString());
            rowItem.getItemProperty("gender").setValue(p.getGender());
            rowItem.getItemProperty("mail").setValue(p.getMail());
            rowItem.getItemProperty("age").setValue(p.getAge());
            rowItem.getItemProperty("title").setValue(p.getTitle() != null ? p.getTitle().getName(): "");
            rowItem.getItemProperty("birth").setValue(p.getBirth());
        }

        table.sort(new Object[]{"familyName"}, new boolean[]{true});
    }

    private HorizontalLayout getAddressLayout(){
        HorizontalLayout addressLayout = new HorizontalLayout();
        addressLayout.setSpacing(true);
        addressLayout.setSizeFull();

        tfPostCode = new TextField("Post Code");
        tfProvince = new TextField("Province");
        tfStreet = new TextField("Street");
        tfDoorNumber = new TextField("Door Number");
        cbCountry = new ComboBox("Country");
        tfStreetNumber = new TextField("Street Number");
        tfCity = new TextField("City");
        cbCountry = new ComboBox("Country");
        loadCountries();

        addressLayout.addComponent(tfStreet);
        addressLayout.addComponent(tfStreetNumber);
        addressLayout.addComponent(tfDoorNumber);
        addressLayout.addComponent(tfProvince);
        addressLayout.addComponent(tfPostCode);
        addressLayout.addComponent(tfCity);
        addressLayout.addComponent(cbCountry);

        return addressLayout;
    }

    private HorizontalLayout getFilterLayout() {
        HorizontalLayout filterLayout = new HorizontalLayout();
        filterLayout.setSpacing(true);
        filterLayout.setSizeFull();

        tfFamilyName = new TextField("Family Name");
        tfFirstName = new TextField("First Name");
        tfPersonName = new TextField("Person Name");
        cbGender = new ComboBox("Gender", Arrays.asList("m", "f"));
        tfMail = new TextField("Mail");
        tfAge = new TextField("Age");
        dfBirth = new DateField("Birth");
        dfBirth.setDateFormat("yyyy-MM-dd");
        cbTitle = new ComboBox("Title");
        loadTitles();

        btnFilter = new Button("Filter");
        filterLayout.addComponent(tfFamilyName);
        filterLayout.addComponent(tfFirstName);
        filterLayout.addComponent(tfPersonName);
        filterLayout.addComponent(cbGender);
        filterLayout.addComponent(tfMail);
        filterLayout.addComponent(tfAge);
        filterLayout.addComponent(cbTitle);
        filterLayout.addComponent(dfBirth);

        btnFilter.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Person person = new Person();
                person.setFamilyName(tfFamilyName.getValue());
                person.setFirstName(tfFirstName.getValue());
                person.setMail(tfMail.getValue());
                person.setPersonName(tfPersonName.getValue());
                person.setGender(cbGender.getValue() != null ? cbGender.getValue().toString() : null);
                person.setAge(tfAge.getValue());
                person.setTitle((Title) cbTitle.getValue());
                person.setBirth(dfBirth.getValue());

                Address address = new Address();
                address.setDoorNumber(tfDoorNumber.getValue());
                address.setStreetNumber(tfStreetNumber.getValue());
                address.setStreet(tfStreet.getValue());
                address.setProvince(tfProvince.getValue());
                address.setPostCode(tfPostCode.getValue());
                address.setCity(tfCity.getValue());
                address.setCountry((Country) cbCountry.getValue());

                person.setAddress(address);

                List<Person> persons = personService.filterPersons(person);

                addPersons(persons);
            }
        });

        return filterLayout;
    }

    /**
     * Is called by a navigator and calls {@link #init()} to initialize the components for this view.
     * @param viewChangeEvent contains the parameters of the call
     */
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        removeAllComponents();
        init();
    }
}
