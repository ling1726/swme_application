package at.ac.tuwien.inso.services.interfaces;


import at.ac.tuwien.inso.entities.Title;

import java.util.List;

/**
 * This service provides methods to select title entries from the database.
 * @author Lingfan Gao
 */
public interface TitleService {

    /**
     * Selects all persisted titles
     * @return list of all titles
     */
    public List<Title> getTitles();

    /**
     * Selects exactly one title entry by the given identifier.
     * @param id unique identifier
     * @return title matching the identifier
     */
    public Title getTitleById(Long id);

    /**
     * Selects exactly one title entry by the given name.
     * @param name full title name
     * @return selected title
     */
    public Title getTitleByName(String name);
}
