package ac.at.tuwien.inso.service;

import ac.at.tuwien.inso.base.AbstractSpringBootTest;
import at.ac.tuwien.inso.entities.Address;
import at.ac.tuwien.inso.entities.Country;
import at.ac.tuwien.inso.repositories.interfaces.AddressRepository;
import at.ac.tuwien.inso.repositories.interfaces.CountryRepository;
import at.ac.tuwien.inso.services.AddressServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class AddressServiceTest extends AbstractSpringBootTest {

    @Autowired
    private AddressRepository  addressRepository;
    @Autowired
    private CountryRepository  countryRepository;
    @Autowired
    private AddressServiceImpl addressService;

    private Address persistedAddress;
    private Address unpersistedAddress;

    @Before
    public void setUp() throws Exception {
        Country austria = new Country();
        austria.setName("Austria");
        austria.setCode("AT");
        countryRepository.saveAndFlush(austria);

        persistedAddress = new Address();
        persistedAddress.setCountry(austria);
        persistedAddress.setCity("Vienna");
        persistedAddress.setStreet("Mariahilfer Straße");
        persistedAddress.setStreetNumber("1");
        persistedAddress.setDoorNumber("2");
        persistedAddress.setPostCode("1010");

        addressRepository.saveAndFlush(persistedAddress);

        Country middleEarth = new Country();
        middleEarth.setName("Middle Earth");
        middleEarth.setCode("ME");
        countryRepository.saveAndFlush(middleEarth);

        unpersistedAddress = new Address();
        unpersistedAddress.setCountry(middleEarth);
        unpersistedAddress.setCity("The Shire");
        unpersistedAddress.setStreet("Baggins Street");
        unpersistedAddress.setStreetNumber("5");
        unpersistedAddress.setDoorNumber("1");
        unpersistedAddress.setPostCode("12345");
    }

    @Test
    public void insertAddress() throws Exception {
        addressService.insertAddress(unpersistedAddress);
        assertEquals(unpersistedAddress, addressRepository.findOne(unpersistedAddress.getId()));
    }

    @Test
    public void updateAddress() throws Exception {
        long idOfPersistedEntry = persistedAddress.getId();

        // Change some values
        Address fromDB = addressRepository.findOne(idOfPersistedEntry);
        fromDB.setStreetNumber(unpersistedAddress.getStreetNumber());
        fromDB.setStreet(unpersistedAddress.getStreet());
        fromDB.setCity(unpersistedAddress.getCity());
        fromDB.setDoorNumber(unpersistedAddress.getDoorNumber());
        fromDB.setCountry(unpersistedAddress.getCountry());
        fromDB.setPostCode(unpersistedAddress.getPostCode());

        // update
        addressService.updateAddress(fromDB);

        // Entity should now be updated
        assertEquals(unpersistedAddress, addressRepository.findOne(idOfPersistedEntry));
    }

}