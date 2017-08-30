package fr.husta.test.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import fr.husta.test.config.DatabaseConfiguration;
import fr.husta.test.domain.Actor;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DatabaseConfiguration.class})
@ActiveProfiles("TEST")
public class GenericExtractingTest {

    private static final Logger log = LoggerFactory.getLogger(GenericExtractingTest.class);

    @Resource
    private DataSource dataSource;

    @PersistenceContext
    private EntityManager entityManager;

    private Connection cnx;

    private ObjectMapper jsonMapper;

    @Before
    public void setUp() throws Exception {
        assertThat(dataSource).isNotNull();
        assertThat(entityManager).isNotNull();

        cnx = dataSource.getConnection();

        jsonMapper = new ObjectMapper();
        // to enable standard indentation ("pretty-printing"):
        jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);

        jsonMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @After
    public void tearDown() throws Exception {
        cnx.close();
    }

    @Test
    public void test_1() throws Exception {
        String tableName = "actor";

        String sql = "select count(*) from " + tableName + " ";
        PreparedStatement pstmt = cnx.prepareStatement(sql);
        int maxRows = 10000;
        if (maxRows != -1) {
            pstmt.setMaxRows(maxRows);
        }
//        pstmt.setString(1, tableName);

        ResultSet resultSet = pstmt.executeQuery();
        int count = 0;
        if (resultSet.next()) {
            count = resultSet.getInt(1);
        }
        System.out.println("Size = " + count);
    }


    @Test
    public void test_jdbc_json() throws Exception {
        String tableName = "actor";

        String sqlCount = "select count(*) from " + tableName + " ";
        PreparedStatement pstmt = cnx.prepareStatement(sqlCount);
        int maxRows = 50;
        if (maxRows != -1) {
            pstmt.setMaxRows(maxRows);
        }

        ResultSet resultSet = pstmt.executeQuery();
        int count = 0;
        if (resultSet.next()) {
            count = resultSet.getInt(1);
        }
        System.out.println("Size = " + count);
        resultSet.close();
        pstmt.close();


        String sql = "select row_to_json(o) from " + tableName + " o ";
        PreparedStatement pstmt2 = cnx.prepareStatement(sql);
        if (maxRows != -1) {
            pstmt2.setMaxRows(maxRows);
        }

        ResultSet resultSet2 = pstmt2.executeQuery();
        List<String> listJson = new ArrayList<>();
        while (resultSet2.next()) {
            listJson.add(resultSet2.getString(1));
        }
        for (String s : listJson) {
            System.out.println(" -> " + s);
        }
        resultSet2.close();
        pstmt2.close();
    }

    @Test
    @Ignore("Pas cool : LazyInitializationException")
    public void test_jpa2_criteria() throws Exception {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Actor> criteriaQuery = criteriaBuilder.createQuery(Actor.class);
        criteriaQuery.from(Actor.class);

        List listRes = entityManager.createQuery(criteriaQuery).setMaxResults(10000).getResultList();

        // Pb : org.hibernate.LazyInitializationException (Actor.filmActors)
        for (Object o : listRes) {
            System.out.println("-> " + mapToJson(o));
        }
    }

    private String mapToJson(Object o) {
        try {
            return jsonMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
