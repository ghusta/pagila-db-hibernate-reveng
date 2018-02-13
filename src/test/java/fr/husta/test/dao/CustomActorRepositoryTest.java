package fr.husta.test.dao;

import fr.husta.test.config.DatabaseConfiguration;
import fr.husta.test.domain.Actor;
import fr.husta.test.domain.Actor_;
import fr.husta.test.dto.ActorNamesDto;
import fr.husta.test.dto.ActorNamesDtoImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DatabaseConfiguration.class})
@ActiveProfiles("TEST")
@Transactional
@Rollback
public class CustomActorRepositoryTest {

    private Logger logger = LoggerFactory.getLogger(CustomActorRepositoryTest.class);

    @Autowired
    private CustomActorRepository customActorRepository;

    @Before
    public void setUp() throws Exception {
        assertThat(customActorRepository).isNotNull();
    }

    @Test
    public void customFindByFirstNameStartWithByPage_page1() throws Exception {
        int pageNumber = 1;
        Page<Actor> actors = customActorRepository.customFindByFirstNameStartWithByPage("A",
                PageRequest.of(pageNumber - 1, 3, new Sort(Sort.Direction.ASC, Actor_.firstName.getName())));
        assertThat(actors).isNotEmpty();
        assertThat(actors).hasSize(3);

        logger.debug("PAGE {}", pageNumber);
        for (Actor actor : actors) {
            logger.debug("{} {} {}", actor.getActorId(), actor.getFirstName(), actor.getLastName());
        }
    }

    @Test
    public void customFindByFirstNameStartWithByPage_page2() throws Exception {
        int pageNumber = 2;
        Page<Actor> actors = customActorRepository.customFindByFirstNameStartWithByPage("A",
                PageRequest.of(pageNumber - 1, 3, new Sort(Sort.Direction.ASC, Actor_.firstName.getName())));
        assertThat(actors).isNotEmpty();
        assertThat(actors).hasSize(3);

        logger.debug("PAGE {}", pageNumber);
        for (Actor actor : actors) {
            logger.debug("{} {} {}", actor.getActorId(), actor.getFirstName(), actor.getLastName());
        }
    }

    @Test
    public void customFindByFirstNameStartWithByPage_page3_JpaSort() throws Exception {
        int pageNumber = 3;
        Page<Actor> actors = customActorRepository.customFindByFirstNameStartWithByPage("A",
                PageRequest.of(pageNumber - 1, 3, new JpaSort(Actor_.firstName)));
        assertThat(actors).isNotEmpty();
        assertThat(actors).hasSize(3);

        logger.debug("PAGE {}", pageNumber);
        for (Actor actor : actors) {
            logger.debug("{} {} {}", actor.getActorId(), actor.getFirstName(), actor.getLastName());
        }
    }

    @Test
    public void customFindByFirstNameStartWithByPage_lastPage_JpaSort() throws Exception {
        int pageNumber = 1;
        Page<Actor> actors = customActorRepository.customFindByFirstNameStartWithByPage("Z",
                PageRequest.of(pageNumber - 1, 3, new JpaSort(Actor_.firstName)));
        assertThat(actors).isNotEmpty();
        assertThat(actors).hasSize(1);

        logger.debug("PAGE {}", pageNumber);
        for (Actor actor : actors) {
            logger.debug("{} {} {}", actor.getActorId(), actor.getFirstName(), actor.getLastName());
        }
    }

    @Test
    public void projectionIntoDto() throws Exception {
        Collection<ActorNamesDto> actorNamesDtoList = customActorRepository.findActorNamesUsingDto();
        assertThat(actorNamesDtoList).isNotEmpty();

//        for (ActorNamesDto actorNamesDto : actorNamesDtoList) {
//            System.out.println(actorNamesDto);
//        }

    }

}