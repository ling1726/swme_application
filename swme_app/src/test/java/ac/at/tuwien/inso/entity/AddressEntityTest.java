package ac.at.tuwien.inso.entity;

import ac.at.tuwien.inso.entity.base.AbstractEntityTest;
import at.ac.tuwien.inso.entities.Address;
import at.ac.tuwien.inso.entities.Country;
import at.ac.tuwien.inso.repositories.interfaces.AddressRepository;
import at.ac.tuwien.inso.repositories.interfaces.CountryRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public class AddressEntityTest extends AbstractEntityTest {

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private AddressRepository addressRepository;

    private Address address;
    private Country country;
    
    @Before
    public void setUp() throws Exception {
        address = new Address();
        address.setCity("Nowhere");
        address.setPostCode("12345");
        address.setDoorNumber("5");
        address.setStreet("Janeston Street");
        address.setStreetNumber("10");

        country = new Country();
        country.setName("Narnia");
        country.setCode("NA");
        
        countryRepository.saveAndFlush(country);
        address.setCountry(country);
        addressRepository.saveAndFlush(address);
    }

    @Override
    protected JpaRepository<Address, Long> getEntityRepository() {
        return addressRepository;
    }

    @Test
    public void testStreetNotEmpty() {
        address.setStreet("");
        assertValidationFails(address, "street");
    }

    @Test
    public void testStreetMinSizeTwo() {
        address.setStreet("A");
        assertValidationFails(address, "street");

        address.setStreet("AB");
        assertValidationPasses(address, "street");
    }

    @Test
    public void testStreetNumberNotNull() {
        address.setStreetNumber(null);
        assertValidationFails(address, "streetNumber");
    }

    @Test
    public void testStreetNumberMaxSizeFive() {
        address.setStreetNumber("123456");
        assertValidationFails(address, "streetNumber");

        address.setStreetNumber("12345");
        assertValidationPasses(address, "streetNumber");
    }

    @Test
    public void testDoorNumberMaxSizeTwo() {
        address.setDoorNumber("ABC");
        assertValidationFails(address, "doorNumber");

        address.setDoorNumber("AB");
        assertValidationPasses(address, "doorNumber");
    }

    @Test
    public void testPostCodeNotNull() {
        address.setPostCode(null);
        assertValidationFails(address, "postCode");
    }
    
    @Test
    public void testPostCodeMaxSizeSix() {
        address.setPostCode("1234567");
        assertValidationFails(address, "postCode");

        address.setPostCode("123456");
        assertValidationPasses(address, "postCode");
    }

    @Test
    public void testCityNotEmpty() {
        address.setCity("");
        assertValidationFails(address, "city");
    }
}
