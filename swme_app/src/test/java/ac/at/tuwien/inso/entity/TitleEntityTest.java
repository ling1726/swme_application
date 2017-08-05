package ac.at.tuwien.inso.entity;

import ac.at.tuwien.inso.entity.base.AbstractEntityTest;
import at.ac.tuwien.inso.entities.Title;
import at.ac.tuwien.inso.repositories.interfaces.TitleRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public class TitleEntityTest extends AbstractEntityTest {

    @Autowired
    private TitleRepository   titleRepository;

    @Override
    protected JpaRepository<Title, Long> getEntityRepository() {
        return titleRepository;
    }

    @Test
    public void testNameNotEmpty() {
        Title title = new Title();
        title.setName("");
        assertValidationFails(title, "name");
    }
    
    @Test
    public void testNameMinSizeTwo() {
        Title title = new Title();
        assertValidationFails(title, "name");

        title.setName("A");
        assertValidationFails(title, "name");
        
        title.setName("AB");
        assertValidationPasses(title, "name");
    }

}
