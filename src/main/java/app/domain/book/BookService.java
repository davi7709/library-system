package app.domain.book;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BookService {
    private final Logger logger = LoggerFactory.getLogger(BookService.class);
    private final BookRepository queries = new BookRepository();

    public List<Book> findAll(){
        return queries.findAll();
    }
    public Book findByIsbn(String isbn){
        return queries.findByIsbn(isbn);
    }
    public Book saveBook(Book book) throws IllegalStateException{
        if(queries.findByIsbn(book.isbn()) != null){
            throw new IllegalStateException("Livro ja esta cadastrado");
        }else{
            return queries.save(book);
        }
    }
    public boolean deleteBook(String isbn){
        return queries.deleteByIsbn(isbn);
    }

}
