package ac.at.tuwien.inso.service;

import ac.at.tuwien.inso.base.AbstractSpringBootTest;
import at.ac.tuwien.inso.entities.Country;
import at.ac.tuwien.inso.repositories.interfaces.CountryRepository;
import at.ac.tuwien.inso.services.interfaces.CountryService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CountryServiceTest extends AbstractSpringBootTest {

    @Autowired
    private CountryRepository  countryRepository;
    @Autowired
    private CountryService countryService;

    private Country hyrule;

    @Before
    public void setUp() throws Exception {
        hyrule = new Country();
        hyrule.setName("Hyrule");
        hyrule.setCode("HY");
        countryRepository.saveAndFlush(hyrule);

        Country middleEarth = new Country();
        middleEarth.setName("Middle Earth");
        middleEarth.setCode("ME");
        countryRepository.saveAndFlush(middleEarth);
    }

    @Test
    public void getCountries() throws Exception {
        List<Country> allCountries = countryService.getCountries();
        assertTrue(allCountries.size() >= 2);

    }

    @Test
    public void getCountryById() throws Exception {
        Country fetchedCountry = countryService.getCountryById(hyrule.getId());
        assertEquals(hyrule, fetchedCountry);
    }

    @Test
    public void getCountryByName() throws Exception {
        Country fetchedCountry = countryService.getCountryByName(hyrule.getName());
        assertEquals(hyrule, fetchedCountry);
    }

}