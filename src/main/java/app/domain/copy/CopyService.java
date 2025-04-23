package app.domain.copy;

import app.domain.book.Book;
import app.domain.book.BookRepository;
import app.domain.enums.Status;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CopyService {
    private final Logger logger = LoggerFactory.getLogger(CopyService.class);

    private final CopyRepository queries = new CopyRepository();

    private final BookRepository bookRepository = new BookRepository();

    public List<Copy> findAll(){
        return queries.findAll();
    }

    public Copy save(@NotNull Copy copy){
        if(copy.status() == null){
            throw new IllegalArgumentException("Status invalido");
        }
        if (copy.book() == null || copy.book().isbn() == null) {
            throw new IllegalArgumentException("Livro associado é obrigatório");
        }

        Book bookExistent = bookRepository.findByIsbn(copy.book().isbn());
        if (bookExistent == null) {
            throw new IllegalArgumentException("Livro não encontrado no sistema");
        }
        return queries.save(copy);
    }

    public List<Copy> findAllByIsbn(@NotNull String isbn){
        if(isbn.isBlank()){
            throw new IllegalArgumentException("ISBN invalido");
        }
        return queries.findAllByBook(isbn);
    }

    public List<Copy> findAllByStatus(Status status){
        if(status == null){
            throw new IllegalArgumentException("O status nao pode ser nulo");
        }
        return queries.findAllByStatus(status);
    }

    public boolean delete(Long id){
        return queries.deleteById(id);
    }

    public boolean delete(String id){
        return delete(Long.valueOf(id));
    }
}
