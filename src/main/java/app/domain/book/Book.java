package app.domain.book;

import app.domain.enums.Genre;


public record Book (String isbn, String title, String author, String description, Genre genre){
    public Genre getGenre() { return genre; }
    public String getIsbn() {
        return isbn;
    }
}
