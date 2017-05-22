package fr.husta.test.database;

import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Inspiré par {@link org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest @DataJpaTest}.
 * <p>
 * Test {@link Transactional @Transactional} (Spring).
 * </p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Transactional
public @interface TestDatabase
{
}
