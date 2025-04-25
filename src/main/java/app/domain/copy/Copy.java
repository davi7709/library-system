package app.domain.copy;

import app.domain.book.Book;
import app.domain.enums.Status;

public record Copy (Long id, Book book, Status status){
    public String getIsbn() {
        return book.getIsbn();
    }

    public Long getCopyId(){return id;}
}
