package com.sumioturk.satomi.domain;

import java.util.List;

/**
 * AsyncRepository interface
 */
public interface AsyncRepository<T> {


    public interface RepositoryAsyncCallback<T> {

        void onEntity(T entity);

        void onError(Exception e);
    }

    /**
     * Resolve entity with given id
     * @param id identification
     * @return {@link T}
     */
    void resolve(String id, RepositoryAsyncCallback<T> callback);

    /**
     * Resolve all entities in the repository
     * @return
     */
    void resolveAll(RepositoryAsyncCallback<List<T>> callback);

    /**
     * Store entity
     * @param obj
     */
    void store(T obj, RepositoryAsyncCallback<T> callback);

}
