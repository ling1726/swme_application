package at.ac.tuwien.inso.fe;

import at.ac.tuwien.inso.entities.Address;
import at.ac.tuwien.inso.entities.Country;
import at.ac.tuwien.inso.entities.Person;
import at.ac.tuwien.inso.entities.Title;
import at.ac.tuwien.inso.services.CountryServiceImpl;
import at.ac.tuwien.inso.services.PersonServiceImpl;
import at.ac.tuwien.inso.services.TitleServiceImpl;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.DateRangeValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * This view contains all UI components used to edit or insert a person.
 */
@UIScope
@SpringView(name = MainUI.PERSON_EDIT_VIEW)
public class PersonEditView extends FormLayout implements View {
    @Autowired
    private PersonServiceImpl personService;
    @Autowired
    private TitleServiceImpl titleService;
    @Autowired
    private CountryServiceImpl countryService;


    private Long personId;
    private Person person;
    private Address address;

    private TextField tfFirstName;
    private TextField tfFamilyName;
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

    private Button btnSave;
    private Button btnCancel;
    private Button btnDelete;

    private boolean insert = false;

    @PostConstruct
    public void init(){
        this.setMargin(true);

    }

    /**
     * Is called by a navigator and initializes components for this view.
     * Checks the given parameter for a valid number which references a
     * person to be updated. Else the form is used for creating a new person.
     * @param viewChangeEvent contains the given parameter for update
     */
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        removeAllComponents();
        insert = false;

        String parameters = viewChangeEvent.getParameters();

        try {
            personId = Long.parseLong(parameters);
        } catch (NumberFormatException e) {
            insert = true;
        }

        loadPerson();
        createForm();
    }

    private void loadPerson() {
        if (insert) {
            person = new Person();
            address = new Address();
        } else {
            person = personService.findPersonById(personId);
            address = person.getAddress();
        }
    }

    private void loadTitles(){
        List<Title> titles = titleService.getTitles();
        for(Title title: titles){
            cbTitle.addItem(title);
            if(person.getTitle() != null && title.getId() == person.getTitle().getId()){
                cbTitle.setValue(title);
            }
        }
        cbTitle.setItemCaptionMode(AbstractSelect.ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
    }

    private void loadCountries(){
        List<Country> countries = countryService.getCountries();
        for(Country country: countries){
            cbCountry.addItem(country);
            if(person.getAddress() != null &&
                person.getAddress().getCountry() != null &&
                country.getId() == person.getAddress().getCountry().getId()){
                cbCountry.setValue(country);
            }
        }
        cbCountry.setItemCaptionMode(AbstractSelect.ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
    }

    /**
     * Initializes a form representing the persons editable fields. This method also
     * defines actions to the submission buttons 'Cancel', 'Insert/Update', and 'Delete'.
     * It marks required input and performs field specific input validation.
     */
    private void createForm() {
        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.setSpacing(true);

        tfFirstName = new TextField("First Name", person.getFirstName());
        tfFirstName.setNullRepresentation("");
        tfFirstName.setRequired(true);
        tfFirstName.setRequiredError("First name must be filled in!");
        tfFirstName.addValidator(value ->{
            if(!StringUtils.isAlphaSpace((String)value)){
                throw new Validator.InvalidValueException("First Name must only contain aphabetical letters");
            }
        });
        tfFirstName.setImmediate(true);


        tfFamilyName = new TextField("Family Name", person.getFamilyName());
        tfFamilyName.setNullRepresentation("");
        tfFamilyName.setRequired(true);
        tfFamilyName.setRequiredError("Family name must be filled in!");
        tfFamilyName.addValidator(value ->{
            if(!StringUtils.isAlphaSpace((String)value)){
                throw new Validator.InvalidValueException("Family Name must only contain aphabetical letters");
            }
        });
        tfFamilyName.setImmediate(true);

        tfMail = new TextField("Mail", person.getMail());
        tfMail.setNullRepresentation("");
        tfMail.setRequired(true);
        tfMail.setRequiredError("Mail address must be filled in!");
        tfMail.addValidator(new RegexpValidator("[A-Z0-9a-z.]+@[A-Za-z0-9.]+\\.[A-Za-z]{2,3}", "Invalid mail address"));
        tfMail.setImmediate(true);

        tfAge = new TextField("Age", person.getAge() == null ? null : person.getAge());
        tfAge.setNullRepresentation("");
        tfAge.setReadOnly(true);
        tfAge.setVisible(!insert);

        cbTitle = new ComboBox("Title");
        loadTitles();

        cbGender = new ComboBox("Gender", Arrays.asList("m", "f"));
        cbGender.setRequired(true);
        cbGender.setRequiredError("A gender must be picked !");
        cbGender.setValue(person.getGender());

        dfBirth = new DateField("Birth");
        dfBirth.setValue(person.getBirth());
        dfBirth.setDateFormat("yyyy-MM-dd");
        dfBirth.setRequired(true);
        dfBirth.setRequiredError("Date of birth must be filled in!");
        dfBirth.addValidator(new DateRangeValidator("Birthday must be in the past", null, new Date(), Resolution.DAY));
        dfBirth.setImmediate(true);

        tfPersonName = new TextField("Person Name");
        tfPersonName.setValue(person.getPersonName());
        tfPersonName.setReadOnly(true);
        tfPersonName.setVisible(!insert);

        tfStreet = new TextField("Street", person.getAddress() != null ? person.getAddress().getStreet() : "");
        tfStreet.setRequired(true);
        tfStreet.setRequiredError("Street name must be filled in!");
        tfStreet.addValidator(value -> {
            if(!StringUtils.isAsciiPrintable((String) value)){
                throw new Validator.InvalidValueException("Street name must only contain letters");
            }
        });
        tfStreet.setImmediate(true);

        tfStreetNumber = new TextField("Street Number", person.getAddress() != null ? person.getAddress().getStreetNumber() : "");
        tfStreetNumber.setRequired(true);
        tfStreetNumber.setRequiredError("Street number must be filled in!");
        tfStreetNumber.addValidator(value -> {
            if(!StringUtils.isAlphanumericSpace((String) value)){
                throw new Validator.InvalidValueException("Street number must only contain numbers and letters");
            }
        });
        tfStreetNumber.setImmediate(true);

        tfDoorNumber = new TextField("Door Number", person.getAddress() != null ? person.getAddress().getDoorNumber(): "");
        tfDoorNumber.setNullRepresentation("");
        tfDoorNumber.addValidator(value -> {
            if(!StringUtils.isAlphanumericSpace((String) value) && !(value == null)){
                throw new Validator.InvalidValueException("Door number must only contain numbers and letters");
            }
        });
        tfDoorNumber.setImmediate(true);

        tfCity = new TextField("City", person.getAddress() != null ? person.getAddress().getCity() : "");
        tfCity.setRequired(true);
        tfCity.setRequiredError("City must be filled in!");
        tfCity.addValidator(value -> {
            if(!StringUtils.isAlphaSpace((String) value)){
                throw new Validator.InvalidValueException("City name must only contain letters");
            }
        });
        tfCity.setImmediate(true);

        tfProvince = new TextField("Province", person.getAddress() != null ? person.getAddress().getProvince() : "");
        tfProvince.setNullRepresentation("");
        tfProvince.addValidator(value -> {
            if(!StringUtils.isAlphaSpace((String) value)&&!(value == null)){
                throw new Validator.InvalidValueException("Provincee must only contain letters");
            }
        });
        tfProvince.setImmediate(true);

        tfPostCode = new TextField("PLZ", person.getAddress() != null ? person.getAddress().getPostCode(): "");
        tfPostCode.setRequired(true);
        tfPostCode.setRequiredError("Postcode must be filled in!");
        tfPostCode.addValidator(value -> {
            if(!StringUtils.isAlphanumericSpace((String) value)){
                throw new Validator.InvalidValueException("Postcode must only contain numbers and letters");
            }
        });
        tfPostCode.setImmediate(true);

        cbCountry = new ComboBox("Country");
        loadCountries();


        addComponent(tfFirstName);
        addComponent(tfFamilyName);
        addComponent(tfPersonName);
        addComponent(tfMail);
        addComponent(tfAge);
        addComponent(cbTitle);
        addComponent(cbGender);
        addComponent(dfBirth);
        addComponent(tfStreet);
        addComponent(tfStreetNumber);
        addComponent(tfDoorNumber);
        addComponent(tfCity);
        addComponent(tfProvince);
        addComponent(tfPostCode);
        addComponent(cbCountry);

        btnCancel = new Button("Cancel");
        btnCancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                UI.getCurrent().getNavigator().navigateTo(MainUI.PERSONS_VIEW);
            }
        });

        btnSave = new Button(insert ? "Insert" : "Update");
        btnSave.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Iterator componentIt = iterator();
                while(componentIt.hasNext()){
                    Component component = (Component) componentIt.next();
                    if(Field.class.isAssignableFrom(component.getClass())){
                        if(!((Field) component).isValid()) return;
                    }
                }

                person.setFirstName(tfFirstName.getValue());
                person.setFamilyName(tfFamilyName.getValue());
                person.setTitle((Title)cbTitle.getValue());
                person.setGender(cbGender.getValue().toString());
                person.setMail(tfMail.getValue());
                person.setBirth(dfBirth.getValue());

                address.setStreet(tfStreet.getValue());
                address.setProvince(tfProvince.getValue());
                address.setCity(tfCity.getValue());
                address.setDoorNumber(tfDoorNumber.getValue());
                address.setPostCode(tfPostCode.getValue());
                address.setStreetNumber(tfStreetNumber.getValue());
                address.setCountry((Country) cbCountry.getValue());

                if(insert) {
                    if(personService.checkPersonExists(tfMail.getValue())){
                        Notification.show("A person with the given MAIL address already exists!", Notification.Type.ERROR_MESSAGE);
                        return;
                    }

                    person.setAddress(address);
                    personService.insertPerson(person);
                } else {
                    person.setAddress(address);
                    personService.updatePerson(person);
                }

                UI.getCurrent().getNavigator().navigateTo(MainUI.PERSONS_VIEW);
            }
        });

        btnDelete = new Button("Delete");
        btnDelete.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                personService.deletePerson(person);

                UI.getCurrent().getNavigator().navigateTo(MainUI.PERSONS_VIEW);
            }
        });

        btnLayout.addComponent(btnSave);
        if(!insert) {
            btnLayout.addComponent(btnDelete);
        }
        btnLayout.addComponent(btnCancel);

        addComponent(btnLayout);
    }
}
