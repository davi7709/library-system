package app.domain.book;

import java.util.List;

public interface IBookRepository {
    Book save(Book book);
    boolean deleteByIsbn(String isbn);
    Book findByIsbn(String isbn);
    List<Book> findAll();

}
