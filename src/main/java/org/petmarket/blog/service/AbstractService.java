package org.petmarket.blog.service;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AbstractService<T, L> {
    T save(L l);

    T get(Long id);

    List<T> getAll(Pageable pageable);

    void delete(Long id);

    T updateById(Long id, L l);
}
