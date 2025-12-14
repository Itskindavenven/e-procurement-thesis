package com.tugas_akhir.procurement_service.common.base;

import java.util.List;
import java.util.Optional;

public interface BaseService<T, ID> {
    T create(T dto);

    T update(ID id, T dto);

    void delete(ID id);

    T findById(ID id);

    List<T> findAll();
}

