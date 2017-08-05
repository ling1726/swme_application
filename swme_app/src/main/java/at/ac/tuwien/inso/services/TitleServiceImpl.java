package at.ac.tuwien.inso.services;


import at.ac.tuwien.inso.entities.Title;
import at.ac.tuwien.inso.repositories.interfaces.TitleRepository;
import at.ac.tuwien.inso.services.interfaces.TitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * {@inheritDoc}
 */
@Service
public class TitleServiceImpl implements TitleService {

    @Autowired
    private TitleRepository titleRepository;
    
    @Override
    public List<Title> getTitles() {
        return titleRepository.findAll();
    }

    @Override
    public Title getTitleById(Long id){ return titleRepository.findOne(id);}

    @Override
    public Title getTitleByName(String name) {
        return titleRepository.findTitleByName(name);
    }
}
