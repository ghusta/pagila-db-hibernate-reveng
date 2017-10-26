package fr.husta.test.dao;

import fr.husta.test.domain.Actor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomActorRepository extends ActorRepository {

    /**
     * Query could also be " like ?1% "
     * <br/>
     * See : https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query
     *
     * @param startWith
     * @param pageable
     * @return
     */
    @Query(value = "select o from Actor o where o.firstName like :startWith% ", nativeQuery = false)
    Page<Actor> customFindByFirstNameStartWithByPage(@Param("startWith") String startWith, Pageable pageable);

}
