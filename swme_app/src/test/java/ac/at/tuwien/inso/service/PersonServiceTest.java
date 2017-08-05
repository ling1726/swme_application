package ac.at.tuwien.inso.service;

import ac.at.tuwien.inso.base.AbstractSpringBootTest;
import at.ac.tuwien.inso.entities.Address;
import at.ac.tuwien.inso.entities.Country;
import at.ac.tuwien.inso.entities.Person;
import at.ac.tuwien.inso.entities.Title;
import at.ac.tuwien.inso.repositories.interfaces.CountryRepository;
import at.ac.tuwien.inso.repositories.interfaces.PersonRepository;
import at.ac.tuwien.inso.services.AddressServiceImpl;
import at.ac.tuwien.inso.services.PersonServiceImpl;
import at.ac.tuwien.inso.services.TitleServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class PersonServiceTest extends AbstractSpringBootTest {

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private PersonRepository  personRepository;

    @Autowired
    PersonServiceImpl  personService;
    @Autowired
    AddressServiceImpl addressService;
    @Autowired
    TitleServiceImpl   titleService;

    private Person jane;

    @Before
    public void setUp() throws Exception {
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

    @Test
    public void testGetPersons() {

        Country middleEarth = new Country();
        middleEarth.setName("Middle Earth");
        middleEarth.setCode("ME");
        countryRepository.saveAndFlush(middleEarth);

        List<Person> persons = personService.getPersons();

        Assert.assertTrue(persons.size() > 0);
    }

    @Test
    public void testInsertPerson() {
        Title   t       = titleService.getTitles().get(0);
        Address address = new Address();
        address.setStreet("Reefer Street");
        address.setDoorNumber("12");
        address.setStreetNumber("1234");
        address.setPostCode("420");
        address.setCity("Kingston");
        address.setProvince("Jamaica");

        Person p = new Person("Max", "Mustermann", "max.mustermann@gmail.com", t, "m", new Date());
        p.setAddress(address);
        p.setTitle(t);

        Long personId = personService.insertPerson(p);

        Assert.assertNotNull(personId);
        Assert.assertNotNull(p.getAddress().getId());
    }

    @Test
    public void testFilterPerson() {
        Address address = new Address();
        address.setStreet("Reefer Street");
        address.setDoorNumber("12");
        address.setStreetNumber("1234");
        address.setPostCode("420");
        address.setCity("Kingston");
        address.setProvince("Jamaica");

        Person p = new Person("John", "Doe", "john.doe@gmail.com", null, "m", new Date());
        p.setAddress(address);

        Long personId = personService.insertPerson(p);

        List<Person> personList = personService.filterPersons(p);
        assertEquals(1, personList.size());
        assertEquals(personId, personList.get(0).getId());
    }

    @Test
    public void testFindPersonById() {
        Person fetchedPerson = personService.findPersonById(jane.getId());
        assertEquals(jane.getFamilyName(), fetchedPerson.getFamilyName());
        assertEquals(jane.getFirstName(), fetchedPerson.getFirstName());
    }

    @Test
    public void testUpdatePerson() {
        jane.setFirstName("John");
        jane.setGender("m");

        personService.updatePerson(jane);

        Person updatedPerson = personRepository.findOne(jane.getId());

        // Updated entries should be different
        assertEquals(updatedPerson.getGender(), "m");
        assertEquals(updatedPerson.getFirstName(), "John");
        // Others should stay the same
        assertEquals(updatedPerson.getFamilyName(), jane.getFamilyName());
        assertEquals(updatedPerson.getAddress(), jane.getAddress());
        assertEquals(updatedPerson.getAge(), jane.getAge());
        assertEquals(updatedPerson.getMail(), jane.getMail());
        assertEquals(updatedPerson.getTitle(), jane.getTitle());
        assertEquals(updatedPerson.getPersonName(), "John " + jane.getFamilyName());
    }

    @Test
    public void testDeletePerson() {
        Long id = jane.getId();
        assertNotNull(id);
        
        personService.deletePerson(jane);
        
        assertNull(personRepository.findOne(id));
    }

    @Test
    public void testCheckPersonExists() {
        assertTrue(personService.checkPersonExists("jane@doe.com"));
        assertFalse(personService.checkPersonExists("totallynotexistingmail@totallynotexistingdomain.com"));
    }
}
