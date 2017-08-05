package ac.at.tuwien.inso.service;

import ac.at.tuwien.inso.base.AbstractSpringBootTest;
import at.ac.tuwien.inso.entities.Title;
import at.ac.tuwien.inso.repositories.interfaces.TitleRepository;
import at.ac.tuwien.inso.services.TitleServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TitleServiceTest extends AbstractSpringBootTest {

    @Autowired
    private TitleRepository  titleRepository;
    @Autowired
    private TitleServiceImpl titleService;

    private Title title1;
    private Title title2;
    
    @Before
    public void setUp() throws Exception {
        title1 = new Title();
        title1.setName("BSc");

        title2 = new Title();
        title2.setName("MSc");
        title2.setPreceding(true);
        
        titleRepository.saveAndFlush(title1);
        titleRepository.saveAndFlush(title2);
    }

    @Test
    public void testGetTitles() throws Exception {
        List<Title> allTitles = titleService.getTitles();
        assertTrue(allTitles.size() >= 2);
    }

    @Test
    public void testGetTitleById() throws Exception {
        Title fetchedTitle = titleService.getTitleById(title1.getId());
        assertEquals(title1, fetchedTitle);
    }

    @Test
    public void testGetTitleByName() throws Exception {
        Title fetchedTitle = titleService.getTitleByName(title1.getName());
        assertEquals(title1, fetchedTitle);
    }

}