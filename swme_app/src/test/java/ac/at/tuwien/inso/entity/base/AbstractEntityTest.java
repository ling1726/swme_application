package ac.at.tuwien.inso.entity.base;

import ac.at.tuwien.inso.base.AbstractSpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.ConstraintViolationException;
import java.io.Serializable;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Abstract class with helper methods for easier testing of entities in terms of validity.
 */
public abstract class AbstractEntityTest extends AbstractSpringBootTest {

    /**
     * Invokes {@link JpaRepository#saveAndFlush(Object)} with the given entity as parameter
     * and asserts an ConstraintViolationException to be thrown. Also asserts that the 
     * <code>fieldName</code> is contained in the message of the ConstraintViolationException
     * (comparison is case insensitive).
     */
    protected void assertValidationFails(Object entity, String fieldName) {
        try {
            getEntityRepository().saveAndFlush(entity);
            fail("Expected ConstraintViolationException but was not thrown. Field: " + fieldName);
        } catch (ConstraintViolationException e) {
            assertTrue(e.getMessage().toLowerCase().contains(fieldName.toLowerCase()));
        }
    }

    /**
     * Invokes {@link JpaRepository#saveAndFlush(Object)} with the given entity as parameter
     * and asserts that <b>no</b> ConstraintViolationException to be thrown.
     */
    protected void assertValidationPasses(Object entity, String fieldName) {
        try {
            getEntityRepository().saveAndFlush(entity);
        } catch (ConstraintViolationException e) {
            fail("Got ConstraintViolationException but expected not to. Field: " + fieldName);
        }
    }

    /**
     * Returns the Spring Repository on which to invoke the saveAndFlush method in the 
     * assertValidation-methods.
     * @see #assertValidationFails(Object, String)
     * @see #assertValidationPasses(Object, String) 
     */
    protected abstract <T> JpaRepository<T, Serializable> getEntityRepository();
}
