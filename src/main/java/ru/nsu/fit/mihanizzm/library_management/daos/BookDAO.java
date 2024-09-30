package ru.nsu.fit.mihanizzm.library_management.daos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.nsu.fit.mihanizzm.library_management.models.Book;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> getAllBooks() {
        return jdbcTemplate.query(
                "select * from books",
                new BookMapper()
        );
    }

    public Optional<Book> getBookById(final int id) {
        return jdbcTemplate.query(
              "select * from books where id = ?",
              new Object[] { id },
                new BookMapper()
        ).stream().findFirst();
    }

    public Optional<Book> getBookByTitle(final String title) {
        return jdbcTemplate.query(
                "select * from books where name = ?",
                new Object[] { title },
                new BookMapper()
        ).stream().findFirst();
    }

    public void saveBook(final Book book) {
        jdbcTemplate.update(
                "insert into books(name, author, year_of_production) values(?, ?, ?)",
                book.getTitle(), book.getAuthor(), book.getYearOfPublication()
        );
    }

    public void updateBook(int id, final Book book) {
        jdbcTemplate.update(
                "update books set name = ?, author = ?, year_of_production = ? where id = ?",
                book.getTitle(), book.getAuthor(), book.getYearOfPublication(), id
        );
    }

    public void deleteBook(final int id) {
        jdbcTemplate.update("delete from books where id = ?", id);
    }

    public List<Book> getAllPersonsBooks(int ownerId) {
        return jdbcTemplate.query(
                "SELECT id, name, author, year_of_production, owner_id FROM books WHERE owner_id = ?",
                new Object[] { ownerId },
                new BookMapper()
        );
    }

    public void assignBookToPerson(final int bookId, final int personId) {
        jdbcTemplate.update("update books set owner_id = ? where id = ?", personId, bookId);
    }

    public void releaseBook(final int bookId) {
        jdbcTemplate.update("update books set owner_id = null where id = ?", bookId);
    }
}
