package ru.nsu.fit.mihanizzm.library_management.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private int id;

    @NotEmpty
    @Size(min = 2, max = 512, message = "Title size must be in range from 2 to 512 symbols")
    private String title;

    @NotEmpty
    @Size(min = 2, max = 256, message = "Author's name length must be in range from 2 to 256 symbols")
    private String author;

    @Min(value = 1, message = "Year of publication must be larger than 0")
    private Integer yearOfPublication;

    private Integer ownerId;
}
