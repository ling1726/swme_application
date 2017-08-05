package at.ac.tuwien.inso.entities;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * This class maps the entries of the Title data table to
 * entities used by the application.
 *
 * @author Lingfan Gao
 */
@Entity
public class Title implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TITLE_SEQ")
    @SequenceGenerator(name="TITLE_SEQ", sequenceName="SQ_TITLE_PK", allocationSize=1)
    @Column(name = "TITLE_PK")
    private long id;
    
    @Column(name="NAME")
    @NotEmpty @Size(min = 2)
    private String name;
    
    @Column(name="IS_PRECEDING")
    private boolean isPreceding;

    /**
     * Default Constructor
     * Creates an empty title entity
     */
    public Title(){

    }

    /**
     * Constructor
     * @param name the long name of the title
     * @param isPreceding describes whether the title is written as prefix or suffix to a persons name
     */
    public Title(String name, boolean isPreceding){
        this.name = name;
        this.isPreceding = isPreceding;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPreceding() {
        return isPreceding;
    }

    public void setPreceding(boolean isPreceding) {
        isPreceding = isPreceding;
    }

    public String toString(){
        return this.name;
    }

    /**
     * This method compares two Titles.
     * @param o an object representation of a title
     * @return 'true' if the titles are equal, 'false' if they differ in a single parameter
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Title title = (Title) o;

        if (isPreceding != title.isPreceding) return false;
        return name != null ? name.equals(title.name) : title.name == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (isPreceding ? 1 : 0);
        return result;
    }
}


