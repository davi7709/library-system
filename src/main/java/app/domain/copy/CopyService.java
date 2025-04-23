package app.domain.copy;

import app.domain.book.Book;
import app.domain.enums.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CopyService {
    private final Logger logger = LoggerFactory.getLogger(CopyService.class);

    private final CopyRepository queries = new CopyRepository();

    public List<Copy> findAll(Copy copy){
        return queries.findAll();
    }

    public Copy save(Copy copy){
        if(copy.status() == null){
            throw new IllegalArgumentException("Status invalido");
        }
        return queries.save(copy);
    }

    public List<Copy> findAllByIsbn(Book book){
        if(book.isbn() == null){
            throw new IllegalArgumentException("Livro invalido");
        }
        return queries.findAllByBook(book);
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
}
