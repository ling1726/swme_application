package at.ac.tuwien.inso.entities;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * This class maps the entries of the Address data table to
 * entities used by the application.
 *
 * @author Lingfan Gao
 */
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADDRESS_SEQ")
    @SequenceGenerator(name="ADDRESS_SEQ", sequenceName="SQ_ADDRESS_PK", allocationSize=1)
    @Column(name = "ADDRESS_PK")
    private long id;

    @Column(name = "STREET")
    @NotEmpty @Size(min = 2)
    private String street;

    @Column(name = "STREET_NUMBER")
    @NotNull @Size(max = 5)
    private String streetNumber;

    @Column(name = "DOOR_NUMBER")
    @Size(max = 2)
    private String doorNumber;

    @Column(name = "PROVINCE")
    private String province;

    @Column(name = "POSTCODE")
    @NotNull @Size(max = 6)
    private String postCode;

    @Column(name = "CITY")
    @NotEmpty
    private String city;

    @ManyToOne(optional = true)
    @JoinColumn(name = "COUNTRY_ID")
    @Valid
    private Country country;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getDoorNumber() {
        return doorNumber;
    }

    public void setDoorNumber(String doorNumber) {
        this.doorNumber = doorNumber;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    /**
     * This method creates an output string containing all parameters of Address.
     * @return a string representation of this entity
     */
    public String toString(){
        String str = this.getStreet() + " " + this.getStreetNumber()+ ", ";
        str += this.getDoorNumber() != null ? this.getDoorNumber() + ", " : "";
        str += "City: "+this.getCity() + ", ";
        str += this.getProvince() != null ? "Province: "+this.getProvince() : "";
        str += "PLZ: "+this.getPostCode();
        str += this.country != null ? ", Country: "+ this.country.toString() + ", " : "";

        return str;
    }

    /**
     * This method compares two Addresses.
     * @param o an object representation of an address
     * @return 'true' if the addresses are equal, 'false' if they differ in a single parameter
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (street != null ? !street.equals(address.street) : address.street != null) return false;
        if (streetNumber != null ? !streetNumber.equals(address.streetNumber) : address.streetNumber != null)
            return false;
        if (doorNumber != null ? !doorNumber.equals(address.doorNumber) : address.doorNumber != null) return false;
        if (province != null ? !province.equals(address.province) : address.province != null) return false;
        if (postCode != null ? !postCode.equals(address.postCode) : address.postCode != null) return false;
        if (city != null ? !city.equals(address.city) : address.city != null) return false;
        return country != null ? country.equals(address.country) : address.country == null;
    }

    @Override
    public int hashCode() {
        int result = street != null ? street.hashCode() : 0;
        result = 31 * result + (streetNumber != null ? streetNumber.hashCode() : 0);
        result = 31 * result + (doorNumber != null ? doorNumber.hashCode() : 0);
        result = 31 * result + (province != null ? province.hashCode() : 0);
        result = 31 * result + (postCode != null ? postCode.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        return result;
    }
}
