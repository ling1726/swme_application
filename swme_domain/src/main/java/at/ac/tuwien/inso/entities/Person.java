package at.ac.tuwien.inso.entities;


import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * This class maps the entries of the Person data table to
 * entities used by the application.
 *
 * @author Lingfan Gao
 */
@Entity
public class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERSON_SEQ")
    @SequenceGenerator(name="PERSON_SEQ", sequenceName="SQ_PERSON_PK", allocationSize=1)
    @Column(name = "PERSON_PK")
    private Long id;
    
    @Column(name = "FIRST_NAME")
    @NotNull
    private String firstName;
    
    @Column(name = "FAMILY_NAME")
    @NotNull
    private String familyName;

    @Transient
    private String personName;
    
    @Column(name = "MAIL_ADDRESS")
    @NotEmpty @Email
    private String mail;
    
    @Transient
    private String age;
    
    @ManyToOne(optional = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "TITLE_ID")
    @Valid
    private Title title;
    
    @Column(name = "GENDER", length = 1)
    @NotNull
    @Pattern(regexp = "m|f")
    private String gender;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "BIRTH")
    @Past
    private Date birth;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "ADDRESS_ID")
    @NotNull @Valid
    private Address address;

    /**
     * Default Constructor
     * Creates an empty person entity
     */
    public Person() {

    }

    /**
     * Constructor
     * @param firstName a persons first name
     * @param familyName a persons last name
     * @param mail a persons email address
     * @param title a title entity {@link Title}
     * @param gender a persons gender ('m' for male or 'f' for female)
     * @param birth a persons date of birth is used to calculate the persons current age
     */
    public Person(String firstName, String familyName, String mail, Title title, String gender, Date birth) {
        this.firstName = firstName;
        this.familyName = familyName;
        this.mail = mail;
        this.title = title;
        this.gender = gender;
        this.birth = birth;
        calculateAge();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    /**
     * This method composes the first and family name of the person.
     * @return the persons full name
     */
    @Transient
    public String getPersonName() {
        if(this.personName != null && !this.personName.isEmpty())
            return this.personName;

        if(stringIsNullOrEmpty(this.firstName) && stringIsNullOrEmpty(this.familyName))
            return null;

        if(!stringIsNullOrEmpty(this.firstName) && !stringIsNullOrEmpty(this.familyName))
            return this.firstName+" "+this.familyName;

        return this.firstName+this.familyName;
    }

    private boolean stringIsNullOrEmpty(String s){
        return (s == null || s.isEmpty());
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }


    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }


    public String getAge() {
        calculateAge();
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }


    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }


    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
        calculateAge();
    }

    /**
     * This method calculates and sets the current age of the person
     * from the given birth date.
     */
    public void calculateAge() {
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        if (this.getBirth() != null) {
            Calendar birthday = Calendar.getInstance();
            birthday.setTime(this.getBirth());
            int age = today.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
            if (birthday.get(Calendar.MONTH) > today.get(Calendar.MONTH) ||
                    (birthday.get(Calendar.MONTH) == today.get(Calendar.MONTH) && birthday.get(Calendar.DATE) > today.get(Calendar.DATE)))
                age--;

            this.setAge(age + "");
        }
    }

    /**
     * This method compares two Persons.
     * @param o an object representation of a person
     * @return 'true' if the persons are equal, 'false' if they differ in a single parameter
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (firstName != null ? !firstName.equals(person.firstName) : person.firstName != null) return false;
        if (familyName != null ? !familyName.equals(person.familyName) : person.familyName != null) return false;
        if (personName != null ? !personName.equals(person.personName) : person.personName != null) return false;
        if (mail != null ? !mail.equals(person.mail) : person.mail != null) return false;
        if (age != null ? !age.equals(person.age) : person.age != null) return false;
        if (title != null ? !title.equals(person.title) : person.title != null) return false;
        if (gender != null ? !gender.equals(person.gender) : person.gender != null) return false;
        if (birth != null ? !birth.equals(person.birth) : person.birth != null) return false;
        return address != null ? address.equals(person.address) : person.address == null;
    }

    @Override
    public int hashCode() {
        int result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + (familyName != null ? familyName.hashCode() : 0);
        result = 31 * result + (personName != null ? personName.hashCode() : 0);
        result = 31 * result + (mail != null ? mail.hashCode() : 0);
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (birth != null ? birth.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }
}

