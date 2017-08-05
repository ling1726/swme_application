package at.ac.tuwien.inso.services;


import at.ac.tuwien.inso.entities.Person;
import at.ac.tuwien.inso.repositories.interfaces.PersonRepository;
import at.ac.tuwien.inso.services.interfaces.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * {@inheritDoc}
 */
@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public List<Person> getPersons() {
        return personRepository.findAll();
    }

    @Override
    @Transactional
    public Long insertPerson(Person person) {
        return personRepository.insertPerson(person).getId();
    }

    @Override
    public Person findPersonById(Long personId) {
        return personRepository.findOne(personId);
    }

    @Override
    @Transactional
    public Person updatePerson(Person person) {
        return personRepository.saveAndFlush(person);
    }

    @Override
    @Transactional
    public void deletePerson(Person person) {
        personRepository.delete(person);
    }

    @Override
    public boolean checkPersonExists(String mail) {
        return personRepository.existsByMail(mail);
    }

    @Override
    public List<Person> filterPersons(Person person) {
        return personRepository.filterPerson(person);
    }
}
