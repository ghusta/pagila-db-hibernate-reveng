package fr.husta.test.config;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Properties;

@Configuration
@PropertySource("classpath:/db.properties")
@EnableJpaRepositories(basePackages = "fr.husta.test.dao")
@EnableTransactionManagement
public class DatabaseConfiguration
{

    @Bean(name = "db.env")
    public String getDbEnv(Environment env)
    {
        return env.getProperty("db.env");
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter, Environment env)
    {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

        if (Arrays.asList(env.getActiveProfiles()).contains("TEST"))
        {
            // pour config test :
            entityManagerFactoryBean.setPersistenceXmlLocation("classpath:META-INF/persistence-test.xml");
        }

        // See also : http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.misc.entity-scanning
        // entityManagerFactoryBean.setPersistenceUnitPostProcessors(...);
        // OU
        // http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/orm/jpa/LocalContainerEntityManagerFactoryBean.html#setPackagesToScan-java.lang.String...-
        // entityManagerFactoryBean.setPackagesToScan(...);
        entityManagerFactoryBean.setPackagesToScan("fr.husta.test");

        Properties jpaProperties = new Properties();
        //If the value of this property is true, Hibernate writes all SQL
        //statements to the console.
        jpaProperties.put(AvailableSettings.SHOW_SQL, env.getProperty("hibernate.show_sql", "false"));
        //If the value of this property is true, Hibernate will format the SQL
        //that is written to the console.
        jpaProperties.put(AvailableSettings.FORMAT_SQL, env.getProperty("hibernate.format_sql", "false"));
        entityManagerFactoryBean.setJpaProperties(jpaProperties);

        return entityManagerFactoryBean;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter()
    {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        // eventuellement set :
        // - setDatabase (voir org.springframework.orm.jpa.vendor.Database)
        // - setDatabasePlatform
        // - setShowSql
        // - setGenerateDdl
        return jpaVendorAdapter;
    }

    @Bean(/* destroyMethod = "close" */)
    public DataSource dataSource(Environment env)
    {
        DataSource ds = null;

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(env.getRequiredProperty("db.url"));
        dataSource.setUsername(env.getRequiredProperty("db.username"));
        dataSource.setPassword(env.getRequiredProperty("db.password"));

        ds = dataSource;

        return ds;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory)
    {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

}
