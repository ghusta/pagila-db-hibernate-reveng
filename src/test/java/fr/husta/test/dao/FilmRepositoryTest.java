package fr.husta.test.dao;

import fr.husta.test.config.DatabaseConfiguration;
import fr.husta.test.database.TestDatabase;
import fr.husta.test.domain.Film;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DatabaseConfiguration.class})
@ActiveProfiles("TEST")
//@TestDatabase // includes @Transactional
@Transactional
@Rollback
public class FilmRepositoryTest
{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FilmRepository filmRepository;

    @Before
    public void setUp() throws Exception
    {
        assertThat(filmRepository).isNotNull();
    }

    @Test
    public void findByReleaseYearBetween() throws Exception
    {
        Integer start = 2006;
        Integer end = 2007;

        List<Film> films = filmRepository.findByReleaseYearBetween(start, end);
        assertThat(films).isNotNull();
        logger.debug("Nb films = {}", films.size());
    }

}

