package app.daos;

import java.util.List;

public interface IDAO<T, I> {

    T getById(I i);
    List<T> getAll();
    T create(T t);
    T update(T t);
    boolean delete(I i);
}