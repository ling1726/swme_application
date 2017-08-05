package at.ac.tuwien.inso.services.interfaces;



import at.ac.tuwien.inso.entities.Person;

import java.util.List;

/**
 * This service provides methods to perform CRUD operations
 * on person entities and to filter or find persisted entries
 * by different search criteria.
 */
public interface PersonService {

    /**
     * Finds a person by a given example person.
     * @param person contains the filter parameters
     * @return list of matching persons
     */
    public List<Person> filterPersons(Person person);

    /**
     * Finds exactly one person by the given identifier.
     * @param personId unique identifier
     * @return selected person
     */
    public Person findPersonById(Long personId);

    /**
     * Selects all persons from the database
     * @return list of all persons
     */
    public List<Person> getPersons();

    /**
     * Creates a new person entry.
     * @param person the person to persist
     * @return id of the persisted entry
     */
    public Long insertPerson(Person person);

    /**
     * Changes the given parameters of one person entry.
     * @param person contains parameters
     * @return the updated person entry
     */
    public Person updatePerson(Person person);

    /**
     * Removes a person entry from the database.
     * @param person contains id of the person to delete
     */
    public void deletePerson(Person person);

    /**
     * Selects a person by the given mail address and returns whether it exists or not.
     * @param mail a person's mail address
     * @return 'true' if mail address was found, 'false' if there is no entry for that mail address
     */
    public boolean checkPersonExists(String mail);
}
