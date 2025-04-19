package app.domain.book;

import app.domain.enums.Genre;


public record Book (String title, String author, String description, String isbn, Genre genre){ }
