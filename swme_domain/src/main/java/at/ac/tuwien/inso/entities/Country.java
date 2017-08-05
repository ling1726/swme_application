package at.ac.tuwien.inso.entities;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * This class maps the entries of the Country data table to
 * entities used by the application.
 *
 * @author Lingfan Gao
 */
@Entity
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COUNTRY_SEQ")
    @SequenceGenerator(name="COUNTRY_SEQ", sequenceName="SQ_COUNTRY_PK", allocationSize=1)
    @Column(name = "COUNTRY_PK")
    private long id;

    @Column(name = "NAME")
    @NotEmpty @Size(min = 4)
    private String name;

    @Column(name = "CODE")
    @NotEmpty @Size(max = 2)
    private String code;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String toString(){
        return this.name;
    }

    /**
     * This method compares two Countries.
     * @param o an object representation of a country
     * @return 'true' if the countries are equal, 'false' if they differ in a single parameter
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Country country = (Country) o;

        if (name != null ? !name.equals(country.name) : country.name != null) return false;
        return code != null ? code.equals(country.code) : country.code == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        return result;
    }
}
