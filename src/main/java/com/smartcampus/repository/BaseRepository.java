// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BaseRepository<T, ID> { // generic in-memory crud for all repos

    protected abstract Map<ID, T> store();
    protected abstract ID getId(T entity);

    public void save(T entity) {
        store().put(getId(entity), entity);
    }

    public T findById(ID id) {
        return store().get(id);
    }

    public List<T> findAll() {
        return new ArrayList<>(store().values());
    }

    public void delete(T entity) {
        store().remove(getId(entity));
    }
}
