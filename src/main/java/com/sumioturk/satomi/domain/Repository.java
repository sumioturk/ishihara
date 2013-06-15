package com.sumioturk.satomi.domain;

import java.util.List;

/**
 * Repository interface
 */
public interface Repository<T> {

    /**
     * Resolve entity with given id
     * @param id identification
     * @return {@link T}
     */
    T resolve(String id);

    /**
     * Resolve all entities in the repository
     * @param id
     * @return
     */
    List<T> resolveAll(String id);

    /**
     * Store entity
     * @param obj
     */
    void store(T obj);

}
