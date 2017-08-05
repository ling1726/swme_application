package at.ac.tuwien.inso.repositories;

import at.ac.tuwien.inso.entities.Person;
import at.ac.tuwien.inso.entities.Title;
import at.ac.tuwien.inso.repositories.interfaces.PersonRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * {@inheritDoc}
 * This class provides methods to filter and insert Persons.
 */
public class PersonRepositoryImpl implements PersonRepositoryCustom {

    @PersistenceContext
    private EntityManager em;


    @Override
    public List<Person> filterPerson(Person person) {
        String queryStr = "SELECT p.* FROM PERSON p " +
                          "LEFT JOIN TITLE t ON p.TITLE_ID = t.TITLE_PK " +
                          "JOIN ADDRESS a ON a.ADDRESS_PK = p.ADDRESS_ID  " +
                          "LEFT JOIN COUNTRY c ON a.COUNTRY_ID = c.COUNTRY_PK";

        List<String> whereParts = new ArrayList<>();

        if(person.getAddress().getStreetNumber() != null && !person.getAddress().getStreetNumber().isEmpty()) {
            whereParts.add("a.STREET_NUMBER like '%" + person.getAddress().getStreetNumber() + "%'");
        }

        if(person.getAddress().getStreet() != null && !person.getAddress().getStreet().isEmpty()) {
            whereParts.add("a.STREET like '%" + person.getAddress().getStreet() + "%'");
        }

        if(person.getAddress().getPostCode() != null && !person.getAddress().getPostCode().isEmpty()) {
            whereParts.add("a.POSTCODE like '%" + person.getAddress().getPostCode() + "%'");
        }

        if(person.getAddress().getDoorNumber() != null && !person.getAddress().getDoorNumber().isEmpty()) {
            whereParts.add("a.DOOR_NUMBER like '%" + person.getAddress().getDoorNumber() + "%'");
        }

        if(person.getAddress().getProvince() != null && !person.getAddress().getProvince().isEmpty()) {
            whereParts.add("a.PROVINCE like '%" + person.getAddress().getProvince() + "%'");
        }

        if(person.getAddress().getCity() != null && !person.getAddress().getCity().isEmpty()) {
            whereParts.add("a.CITY like '%" + person.getAddress().getCity() + "%'");
        }

        if(person.getAddress().getCountry() != null) {
            whereParts.add("c.NAME like '%" + person.getAddress().getCountry().getName() + "%'");
        }

        if(person.getAge() != null && !person.getAge().isEmpty()) {
            whereParts.add("(current_date - p.BIRTH)/365 = " + person.getAge() + "");
        }

        if(person.getBirth() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            whereParts.add("p.BIRTH = '" + sdf.format(person.getBirth()) + "'");
        }

        if(person.getFamilyName() != null && !person.getFamilyName().isEmpty()) {
            whereParts.add("p.FAMILY_NAME like '%" + person.getFamilyName() + "%'");
        }

        if(person.getFirstName() != null && !person.getFirstName().isEmpty()) {
            whereParts.add("p.FIRST_NAME like '%" + person.getFirstName() + "%'");
        }

        if(person.getGender() != null && !person.getGender().isEmpty()) {
            whereParts.add("p.GENDER = '" + person.getGender() + "'");
        }

        if(person.getMail() != null && !person.getMail().isEmpty()) {
            whereParts.add("p.MAIL_ADDRESS like '%" + person.getMail() + "%'");
        }

        if(person.getPersonName() != null && !person.getPersonName().isEmpty()) {
            whereParts.add("(p.FIRST_NAME || ' ' || p.FAMILY_NAME) like '%" + person.getPersonName() + "%'");
        }

        if(person.getTitle() != null && !person.getTitle().getName().isEmpty()) {
            whereParts.add("t.NAME like '%" + person.getTitle().getName() + "%'");
        }

        Iterator<String> it = whereParts.iterator();

        if(it.hasNext()) {
            queryStr += " WHERE ";
        }

        while(it.hasNext()) {
            queryStr += it.next();

            if(it.hasNext()) {
                queryStr += " AND ";
            }
        }

        List<Person> persons = new ArrayList<>();
        try {

            persons = em.createNativeQuery(queryStr, Person.class).getResultList();

        } catch(Exception e) {
            e.printStackTrace();
        }
        return persons;

    }

    @Override
    public Person insertPerson(Person person) {
        Person p = null;
        try{
            Title title = null;
            if(person.getTitle() != null)
                title = em.merge(person.getTitle());

            p = new Person();
            p.setAddress(person.getAddress());
            p.setBirth(person.getBirth());
            p.setFamilyName(person.getFamilyName());
            p.setFirstName(person.getFirstName());
            p.setTitle(title);
            p.setGender(person.getGender());
            p.setMail(person.getMail());
            em.persist(p);
        }catch (Exception e){
            e.printStackTrace();
        }
        return p;
    }

}
