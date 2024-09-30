package ru.nsu.fit.mihanizzm.library_management.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.nsu.fit.mihanizzm.library_management.daos.BookDAO;
import ru.nsu.fit.mihanizzm.library_management.models.Book;
import ru.nsu.fit.mihanizzm.library_management.models.Person;

import java.util.Optional;

@Component
public class BookValidator implements Validator {
    private final BookDAO bookDAO;

    @Autowired
    public BookValidator(final BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Book.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Book book = (Book) o;
        Optional<Book> existingBook = bookDAO.getBookById(book.getId());

        if (
                bookDAO.getBookByTitle(book.getTitle()).isPresent()
                && existingBook.isPresent()
                && existingBook.get().getId() != book.getId()
        ) {
            errors.rejectValue("title", null, "Title already exists");
        }
    }
}
