package at.ac.tuwien.inso.repositories.interfaces;

import at.ac.tuwien.inso.entities.Title;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface provides query methods for CRUD operations on Titles.
 *
 */
public interface TitleRepository extends JpaRepository<Title, Long> {

    /**
     * This method queries the titles by a given name.
     * @param name full name of a title (e.g. 'Bachelor')
     * @return exactly one Title with matching name
     */
    Title findTitleByName(String name);
}
