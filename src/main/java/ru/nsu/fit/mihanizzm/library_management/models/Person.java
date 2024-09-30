package ru.nsu.fit.mihanizzm.library_management.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Person {
    private Integer id;

    @NotEmpty(message = "Name can't be empty")
    @Size(min = 2, max = 256, message = "Name's length should be in range from 2 to 256 symbols")
    private String name;

    @Min(value = 1900, message = "Birth year must be larger or equal 1900")
    @Max(value = 2024, message = "Birth year can't be larger 2024")
    private Integer birthYear;
}
