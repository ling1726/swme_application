package at.ac.tuwien.inso.repositories.interfaces;

import at.ac.tuwien.inso.entities.Person;

import java.util.List;

/**
 * This interface is used to describe custom created queries
 * regarding Persons.
 */
public interface PersonRepositoryCustom  {

    /**
     * This method creates a query string to filter one or more
     * persons by a given example.
     * @param person example person containing the filter parameters
     * @return a list of found entries
     */
    public List<Person> filterPerson(Person person);

    /**
     * This method adds a new Person to the database.
     * @param person the person to add
     * @return the stored person
     */
    public Person insertPerson(Person person);

}
