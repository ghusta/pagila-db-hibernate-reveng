package fr.husta.test.dao;

import fr.husta.test.domain.Actor;
import fr.husta.test.dto.ActorNamesDto;
import fr.husta.test.dto.ActorNamesDtoImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

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

    /**
     * Test for :
     * Spring Data JPA : <b>Query methods with projections</b>.
     * See <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#projections">reference doc</a>.
     * @return
     */
    @Query(value = "select o.firstName as firstName, o.lastName as lastName from Actor o ")
    Collection<ActorNamesDto> findActorNamesUsingDto();

}
