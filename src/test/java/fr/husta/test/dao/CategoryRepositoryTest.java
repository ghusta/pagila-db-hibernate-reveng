package fr.husta.test.dao;

import fr.husta.test.config.DatabaseConfiguration;
import fr.husta.test.database.TestDatabase;
import fr.husta.test.domain.Category;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DatabaseConfiguration.class})
@ActiveProfiles("TEST")
@TestDatabase // includes @Transactional
@Rollback
public class CategoryRepositoryTest
{

    @Autowired
    private CategoryRepository categoryRepository;

    @Before
    public void setUp() throws Exception
    {
        assertThat(categoryRepository).isNotNull();
    }

    @Test
    public void findByName() throws Exception
    {
        String name = "Animation";
        Page<Category> categoryPage1 = categoryRepository.findByName(name, PageRequest.of(0, 100));

        assertThat(categoryPage1).isNotNull();
        assertThat(categoryPage1.getNumberOfElements()).isEqualTo(1);
    }

}