package at.ac.tuwien.inso.repositories.interfaces;

import at.ac.tuwien.inso.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface provides query methods for CRUD operations on Countries.
 *
 */
public interface CountryRepository extends JpaRepository<Country, Long> {

    /**
     * This method queries the countries by a given name.
     * @param name full name of a country (e.g. 'Austria')
     * @return exactly one Country with matching name
     */
    Country findCountryByName(String name);
}
