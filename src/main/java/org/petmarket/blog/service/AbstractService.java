package org.petmarket.blog.service;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AbstractService<T, L> {
    T save(L l, String langCode);

    T get(Long id, String langCode);

    List<T> getAll(Pageable pageable, String langCode);

    void delete(Long id);

    T updateById(Long id, String langCode, L l);
}
