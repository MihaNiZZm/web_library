package ru.nsu.fit.mihanizzm.library_management.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.nsu.fit.mihanizzm.library_management.daos.PersonDAO;
import ru.nsu.fit.mihanizzm.library_management.models.Person;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {
    private final PersonDAO personDAO;

    @Autowired
    public PersonValidator(final PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;
        Optional<Person> existingPerson = personDAO.getPersonByName(person.getName());

        if (
                personDAO.getPersonByName(person.getName()).isPresent()
                && existingPerson.isPresent()
                && existingPerson.get().getId() != person.getId()
        ) {
            errors.rejectValue("name", null, "Person with this name already exists");
        }
    }
}
