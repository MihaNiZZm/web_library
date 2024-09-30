package ru.nsu.fit.mihanizzm.library_management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.mihanizzm.library_management.daos.BookDAO;
import ru.nsu.fit.mihanizzm.library_management.daos.PersonDAO;
import ru.nsu.fit.mihanizzm.library_management.models.Book;
import ru.nsu.fit.mihanizzm.library_management.models.Person;
import ru.nsu.fit.mihanizzm.library_management.util.BookValidator;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BookDAO bookDAO;
    private final BookValidator bookValidator;

    private final PersonDAO personDAO;

    @Autowired
    public BooksController(final BookDAO bookDAO, final BookValidator bookValidator, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.bookValidator = bookValidator;
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("books", bookDAO.getAllBooks());

        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") final int id, Model model) {
        Optional<Book> bookOptional = bookDAO.getBookById(id);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            model.addAttribute("book", book);

            // Проверка на наличие владельца книги
            if (book.getOwnerId() != null) {
                Optional<Person> ownerOptional = personDAO.getPersonById(book.getOwnerId());
                ownerOptional.ifPresent(owner -> model.addAttribute("owner", owner));
            }

            model.addAttribute("people", personDAO.getAllPeople());
        }

        return bookOptional.isPresent() ? "books/show" : "books/not_found";
    }

    @GetMapping("/new")
    public String newBookPage(@ModelAttribute("book") final Book book) {
        return "books/new";
    }

    @PostMapping()
    public String createNewBook(@ModelAttribute("book") @Valid final Book book, BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);

        if (bindingResult.hasErrors()) {
            return "books/new";
        }

        bookDAO.saveBook(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String updateBookPage(@PathVariable("id") final int id, Model model) {
        if (bookDAO.getBookById(id).isPresent()) {
            model.addAttribute("book", bookDAO.getBookById(id).get());
        }

        return bookDAO.getBookById(id).isPresent() ? "books/edit" : "books/not_found";
    }

    @PatchMapping("/{id}")
    public String updateBook(
            @PathVariable("id") final int id,
            @ModelAttribute("person") @Valid final Book book,
            final BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            return "books/edit";
        }
        bookDAO.updateBook(id, book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assignBookToPerson(
            @PathVariable("id") final int id,
            @ModelAttribute("book") final Book book) {
        bookDAO.assignBookToPerson(id, book.getOwnerId());
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") final int id) {
        bookDAO.releaseBook(id);
        return "redirect:/books/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") final int id) {
        bookDAO.deleteBook(id);

        return "redirect:/books";
    }
}
