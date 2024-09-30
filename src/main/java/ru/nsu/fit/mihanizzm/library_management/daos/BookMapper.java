package ru.nsu.fit.mihanizzm.library_management.daos;

import org.springframework.jdbc.core.RowMapper;
import ru.nsu.fit.mihanizzm.library_management.models.Book;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper implements RowMapper<Book> {

    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {
        Book book = new Book();

        book.setId(resultSet.getInt("id"));
        book.setTitle(resultSet.getString("name"));
        book.setAuthor(resultSet.getString("author"));
        book.setYearOfPublication(resultSet.getInt("year_of_production"));
        book.setOwnerId(resultSet.getObject("owner_id", Integer.class));

        return book;
    }
}
