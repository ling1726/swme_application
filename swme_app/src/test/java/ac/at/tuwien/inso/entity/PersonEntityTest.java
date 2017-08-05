package ac.at.tuwien.inso.entity;

import ac.at.tuwien.inso.entity.base.AbstractEntityTest;
import at.ac.tuwien.inso.entities.Address;
import at.ac.tuwien.inso.entities.Country;
import at.ac.tuwien.inso.entities.Person;
import at.ac.tuwien.inso.entities.Title;
import at.ac.tuwien.inso.repositories.interfaces.CountryRepository;
import at.ac.tuwien.inso.repositories.interfaces.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PersonEntityTest extends AbstractEntityTest {

    @Autowired
    private PersonRepository  personRepository;
    @Autowired
    private CountryRepository countryRepository;

    private Person jane;

    @Before
    @SuppressWarnings("Duplicates")
    public void setup() throws Exception {
        jane = new Person();
        jane.setFirstName("Jane");
        jane.setFamilyName("Doe");
        jane.setGender("f");
        jane.setMail("jane@doe.com");
        jane.setBirth(new Date());

        Title title = new Title();
        title.setName("MSc");

        Address address = new Address();
        address.setCity("Nowhere");
        address.setPostCode("12345");
        address.setDoorNumber("5");
        address.setStreet("Janeston Street");
        address.setStreetNumber("10");

        Country country = new Country();
        country.setName("Narnia");
        country.setCode("NA");

        countryRepository.saveAndFlush(country);
        address.setCountry(country);
        jane.setAddress(address);
        jane.setTitle(title);
        personRepository.saveAndFlush(jane);
    }

    @Override
    protected JpaRepository<Person, Long> getEntityRepository() {
        return personRepository;
    }

    @Test
    public void testFirstNameNotNullValidation() {
        jane.setFirstName(null);
        assertValidationFails(jane, "firstName");
    }

    @Test
    public void testFamilyNameNotNullValidation() {
        jane.setFamilyName(null);
        assertValidationFails(jane, "familyName");
    }

    @Test
    public void testMailNotEmpty() {
        jane.setMail("");
        assertValidationFails(jane, "mail");
    }
    
    @Test
    public void testMailAddressValidation() {
        jane.setMail(null);
        assertValidationFails(jane, "mail");

        jane.setMail("NOT_A_VALID_MAIL");
        assertValidationFails(jane, "mail");

        jane.setMail("jane@doe.com");
        assertValidationPasses(jane, "mail");
    }

    @Test
    public void testTitleMustBeValidated() {
        jane.setTitle(new Title());
        assertValidationFails(jane, "title");
    }

    @Test
    public void testGenderNotNullValidation() {
        jane.setGender(null);
        assertValidationFails(jane, "gender");
    }
    
    @Test
    public void testGenderFormatValidation() {
        jane.setGender(null);
        assertValidationFails(jane, "gender");
        
        // Gender Regex mismatch
        jane.setGender("MALE");
        assertValidationFails(jane, "gender");
        jane.setGender("FEMALE");
        assertValidationFails(jane, "gender");
        
        // Valid gender
        jane.setGender("m");
        assertValidationPasses(jane, "gender");
        jane.setGender("f");
        assertValidationPasses(jane, "gender");
    }

    @Test
    public void testBirthInPastValidation() {
        Date dateInFuture = new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(25));
        jane.setBirth(dateInFuture);
        assertValidationFails(jane, "birth");
    }

    @Test
    public void testAddressNotNullValidation() {
        jane.setAddress(null);
        assertValidationFails(jane, "address");
    }
    
    @Test
    public void testAddressMustBeValidated() {
        jane.getAddress().setStreet(null);
        jane.getAddress().setCity(null);
        assertValidationFails(jane, "address");
    }
}
