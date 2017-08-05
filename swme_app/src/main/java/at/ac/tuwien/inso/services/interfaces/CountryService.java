package at.ac.tuwien.inso.services.interfaces;


import at.ac.tuwien.inso.entities.Country;

import java.util.List;

/**
 * This service provides methods to select countries from the database.
 * @author Lingfan Gao
 */
public interface CountryService {

    /**
     * Selects all persistent countries
     * @return list of all countries
     */
    public List<Country> getCountries();

    /**
     * Selects exactly one country by the given identifier.
     * @param id unique identifier
     * @return selected country
     */
    public Country getCountryById(Long id);

    /**
     * Selects exactly one country by the given name.
     * @param name full country name
     * @return selected country
     */
    public Country getCountryByName(String name);
}
