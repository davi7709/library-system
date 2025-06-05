package app.domain.copy;


import app.domain.enums.Status;

import java.util.List;

public interface ICopyRepository{
    Copy save(Copy copy);
    List<Copy> findAll();
    List<Copy> findAllByBook(String isbn);
    List<Copy> findAllByStatus(Status status);
    boolean deleteById(Long id);
}
