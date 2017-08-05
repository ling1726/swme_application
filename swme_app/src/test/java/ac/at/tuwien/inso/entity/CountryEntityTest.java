package ac.at.tuwien.inso.entity;

import ac.at.tuwien.inso.entity.base.AbstractEntityTest;
import at.ac.tuwien.inso.entities.Country;
import at.ac.tuwien.inso.repositories.interfaces.CountryRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public class CountryEntityTest extends AbstractEntityTest {

    @Autowired
    private CountryRepository countryRepository;

    Country country;
    
    @Before
    public void setUp() throws Exception {
        country = new Country();
        country.setName("Austria");
        country.setCode("AT");
    }

    @Override
    protected JpaRepository<Country, Long> getEntityRepository() {
        return countryRepository;
    }

    @Test
    public void testNameNotEmpty() {
        country.setName("");
        assertValidationFails(country, "name");
    }
    
    @Test
    public void testNameMinSizeFour() {
        country.setName(null);
        assertValidationFails(country, "name");

        country.setName("ABC");
        assertValidationFails(country, "name");
        
        country.setName("ABCD");
        assertValidationPasses(country, "name");
    }

    @Test
    public void testCodeNotEmpty() {
        country.setCode("");
        assertValidationFails(country, "code");
    }
    
    @Test
    public void testCodeMaxSizeTwo() {
        country.setCode(null);
        assertValidationFails(country, "code");

        country.setCode("ABC");
        assertValidationFails(country, "code");

        country.setCode("AB");
        assertValidationPasses(country, "code");
    }
}
