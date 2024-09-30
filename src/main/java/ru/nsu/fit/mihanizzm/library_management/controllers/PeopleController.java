package ru.nsu.fit.mihanizzm.library_management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.mihanizzm.library_management.daos.BookDAO;
import ru.nsu.fit.mihanizzm.library_management.daos.PersonDAO;
import ru.nsu.fit.mihanizzm.library_management.models.Person;
import ru.nsu.fit.mihanizzm.library_management.util.PersonValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PersonDAO personDAO;
    private final PersonValidator personValidator;

    private final BookDAO bookDAO;

    @Autowired
    public PeopleController(
            final PersonDAO personDAO,
            final PersonValidator personValidator,
            final BookDAO bookDAO) {
        this.personDAO = personDAO;
        this.personValidator = personValidator;
        this.bookDAO = bookDAO;
    }

    @GetMapping()
    public String index(final Model model) {
        model.addAttribute("people", personDAO.getAllPeople());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") final int id, Model model) {
        if (personDAO.getPersonById(id).isPresent()) {
            model.addAttribute("person", personDAO.getPersonById(id).get());
            model.addAttribute("persons_books", bookDAO.getAllPersonsBooks(id));
        }

        return personDAO.getPersonById(id).isPresent() ? "people/show" : "people/not_found";
    }

    @GetMapping("/new")
    public String newPersonPage(final Model model) {
        model.addAttribute("person", new Person());

        return "people/new";
    }

    @PostMapping()
    public String createNewPerson(
            @ModelAttribute("person") @Valid final Person person,
            final BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "people/new";
        }

        personDAO.insertPerson(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String updatePersonPage(@PathVariable("id") final int id, Model model) {
        if (personDAO.getPersonById(id).isPresent()) {
            model.addAttribute("person", personDAO.getPersonById(id).get());
        }

        return personDAO.getPersonById(id).isPresent() ? "people/edit" : "people/not_found";
    }

    @PatchMapping("/{id}")
    public String updatePerson(
            @PathVariable("id") final int id,
            @ModelAttribute("person") @Valid final Person person,
            final BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "people/edit";
        }
        personDAO.updatePerson(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") final int id) {
        personDAO.deletePerson(id);

        return "redirect:/people";
    }
}
