package at.ac.tuwien.inso.rest_api;

import at.ac.tuwien.inso.entities.Address;
import at.ac.tuwien.inso.entities.Country;
import at.ac.tuwien.inso.entities.Person;
import at.ac.tuwien.inso.entities.Title;
import at.ac.tuwien.inso.rest_api.exceptions.PersonNotFoundException;
import at.ac.tuwien.inso.services.CountryServiceImpl;
import at.ac.tuwien.inso.services.PersonServiceImpl;
import at.ac.tuwien.inso.services.TitleServiceImpl;
import at.ac.tuwien.inso.utils.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by martina on 12.06.17.
 */
@RestController
@RequestMapping({"/persons", "/people"})
public class PersonController {

    @Autowired
    private PersonServiceImpl personService;

    @Autowired
    private CountryServiceImpl countryService;

    @Autowired
    private TitleServiceImpl titleService;

    @RequestMapping(path = "/filterPerson", method = RequestMethod.GET)
    public List<Person> filterperson(@RequestParam(value="family_name", required = false, defaultValue = "") String familyname,
                                     @RequestParam(value="first_name", required = false, defaultValue = "") String firstname,
                                     @RequestParam(value="gender", required = false, defaultValue = "") String gender,
                                     @RequestParam(value="mail", required = false, defaultValue = "") String mail,
                                     @RequestParam(value="age", required = false, defaultValue = "") String age,
                                     @RequestParam(value="birth", required = false, defaultValue = "") String birth,
                                     @RequestParam(value="title", required = false, defaultValue = "") String title,
                                     @RequestParam(value="street", required = false, defaultValue = "") String street,
                                     @RequestParam(value="streetnum", required = false, defaultValue = "") String streetnum,
                                     @RequestParam(value="doornum", required = false, defaultValue = "") String doornum,
                                     @RequestParam(value="province", required = false, defaultValue = "") String province,
                                     @RequestParam(value="post", required = false, defaultValue = "") String post,
                                     @RequestParam(value="city", required = false, defaultValue = "") String city,
                                     @RequestParam(value="country", required = false, defaultValue = "") String country) {

        Person p = new Person();

        try {
            Address address = new Address();
            address.setStreet(street);
            address.setDoorNumber(doornum);
            address.setStreetNumber(streetnum);
            address.setPostCode(post);
            address.setCity(city);
            address.setProvince(province);

            if (country != null & !country.isEmpty()) {
                Country c = countryService.getCountryByName(country);
                address.setCountry(c);
            }

            if (birth != null && !birth.isEmpty()) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                p.setBirth(format.parse(birth));
            }
            p.setFamilyName(familyname);
            p.setFirstName(firstname);
            p.setAddress(address);
            p.setAge(age);
            p.setGender(gender);
            p.setMail(mail);

            if (title != null & !title.isEmpty()) {
                Title t = titleService.getTitleByName(title);
                p.setTitle(t);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return personService.filterPersons(p);
    }

    @GetMapping(path = "/")
    public ResponseEntity<?> index() {
        return new ResponseEntity<>(personService.getPersons(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> showPerson(@PathVariable Long id) {
        Person person = getPersonOrFail(id);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @PostMapping(path = "/")
    public ResponseEntity<?> createPerson(@Valid @RequestBody Person person) {
        personService.updatePerson(person);
        return new ResponseEntity<>(person, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePerson(@PathVariable Long id, @Valid @RequestBody Person person) {
        Person existingPerson = getPersonOrFail(id);
        EntityUtils.copyPropertiesIgnoreNull(person, existingPerson);
        Person saved = personService.updatePerson(existingPerson);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable Long id) {
        Person person = getPersonOrFail(id);
        personService.deletePerson(person);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    private Person getPersonOrFail(Long personId) {
        Person person = this.personService.findPersonById(personId);
        if (person == null) {
            throw new PersonNotFoundException(personId);
        }
        return person;
    }
}
