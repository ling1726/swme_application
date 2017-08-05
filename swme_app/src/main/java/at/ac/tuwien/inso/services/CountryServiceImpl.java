package at.ac.tuwien.inso.services;


import at.ac.tuwien.inso.entities.Country;
import at.ac.tuwien.inso.repositories.interfaces.CountryRepository;
import at.ac.tuwien.inso.services.interfaces.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * {@inheritDoc}
 */
@Service
public class CountryServiceImpl implements CountryService {
    
    @Autowired
    private CountryRepository countryRepository;

    @Override
    public List<Country> getCountries() {
        return this.countryRepository.findAll();
    }

    @Override
    public Country getCountryById(Long id) {
        return this.countryRepository.findOne(id);
    }

    @Override
    public Country getCountryByName(String name) {
        return this.countryRepository.findCountryByName(name);
    }
}
