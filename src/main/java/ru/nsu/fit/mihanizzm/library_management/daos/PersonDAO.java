package ru.nsu.fit.mihanizzm.library_management.daos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.nsu.fit.mihanizzm.library_management.models.Book;
import ru.nsu.fit.mihanizzm.library_management.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> getAllPeople() {
        return jdbcTemplate.query(
                "select * from people",
                new BeanPropertyRowMapper<>(Person.class)
        );
    }

    public Optional<Person> getPersonById(int id) {
        return jdbcTemplate.query(
                "select * from people where id = ?",
                new Object[] { id },
                new BeanPropertyRowMapper<>(Person.class)
        ).stream().findFirst();
    }

    public Optional<Person> getPersonByName(String name) {
        return jdbcTemplate.query(
                "select * from people where name = ?",
                new Object[] { name },
                new BeanPropertyRowMapper<>(Person.class)
        ).stream().findFirst();
    }

    public void insertPerson(Person person) {
        jdbcTemplate.update(
                "insert into people (name, birth_year) values (?, ?)",
                person.getName(), person.getBirthYear()
        );
    }

    public void updatePerson(int id, Person person) {
        jdbcTemplate.update(
                "update people set name = ?, birth_year = ? where id = ?",
                person.getName(), person.getBirthYear(), id
        );
    }

    public void deletePerson(int id) {
        jdbcTemplate.update("delete from people where id = ?", id);
    }
}
