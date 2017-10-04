package fr.husta.test.dao;

import fr.husta.test.config.DatabaseConfiguration;
import fr.husta.test.database.TestDatabase;
import fr.husta.test.domain.Actor;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DatabaseConfiguration.class})
@ActiveProfiles("TEST")
@TestDatabase // includes @Transactional
@Rollback
public class ActorRepositoryTest
{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ActorRepository actorRepository;

    @Before
    public void setUp() throws Exception
    {
        assertThat(actorRepository).isNotNull();
    }

    @Test
    @Commit
    @Ignore
    public void save_usingSequence() throws Exception
    {
        LocalDateTime lastUpdate = LocalDateTime.now();
        Actor newActor = new Actor("Robert", "REDFORD", lastUpdate);

        newActor = actorRepository.save(newActor);

        assertThat(newActor.getActorId()).isNotNull();
    }

    @Test
    public void findById() throws Exception
    {
        Optional<Actor> actor = actorRepository.findById(1);
        assertThat(actor).isPresent();
    }

    @Test
    public void getOneById() throws Exception
    {
        Actor actor = actorRepository.getOne(1);
        assertThat(actor).isNotNull();
    }

    @Test
    public void existsById() throws Exception
    {
        boolean exists = actorRepository.existsById(1);
        assertThat(exists).isTrue();
    }
    @Test
    public void findByFirstName() throws Exception
    {
        String term = "ADAM";
        Page<Actor> actors = actorRepository.findByFirstName(term, new PageRequest(0, 50));
        assertThat(actors).isNotEmpty();
        assertThat(actors.getTotalElements()).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void findByFirstNameLike_noIgnoreCase() throws Exception
    {
        String term = "AdAm";
        List<Actor> actors = actorRepository.findByFirstName(term);
        assertThat(actors).isEmpty();
    }

    @Test
    public void findByFirstNameLikeIgnoreCase() throws Exception
    {
        String term = "AdAm";
        List<Actor> actors = actorRepository.findByFirstNameLikeIgnoreCase(term);
        assertThat(actors).isNotEmpty();
        assertThat(actors.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void findByLastName() throws Exception
    {
        String term = "STALLONE";
        List<Actor> actors = actorRepository.findByLastName(term);
        assertThat(actors).isNotEmpty();
    }

    @Test
    public void findByLastNameIgnoreCase() throws Exception
    {
        String term = "StALLoNe";
        List<Actor> actors = actorRepository.findByLastNameIgnoreCase(term);
        assertThat(actors).isNotEmpty();
    }

    @Test
    public void findByLastNameLike() throws Exception
    {
        String term = "STALLON%";
        List<Actor> actors = actorRepository.findByLastNameLike(term);
        assertThat(actors).isNotEmpty();
    }

}