package at.ac.tuwien.inso.repositories.interfaces;

import at.ac.tuwien.inso.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * This interface provides query methods for CRUD operations on Persons.
 *
 */
public interface PersonRepository extends JpaRepository<Person, Long>, PersonRepositoryCustom {

    /**
     * This method checks whether a given email address exists as entry in the database.
     * @param email email address of a person
     * @return 'true' when email exists, otherwise 'false'
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Person p WHERE p.mail = :email")
    public boolean existsByMail(@Param(value = "email") String email);
    
}

