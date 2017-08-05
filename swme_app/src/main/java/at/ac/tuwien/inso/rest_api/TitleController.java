package at.ac.tuwien.inso.rest_api;

import at.ac.tuwien.inso.services.TitleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ling on 13.06.17.
 */

@RestController
@RequestMapping({"/titles"})
public class TitleController {
    @Autowired
    TitleServiceImpl titleService;

    @GetMapping(path="/")
    public ResponseEntity<?> index(){
        return new ResponseEntity<>(titleService.getTitles(), HttpStatus.OK);
    }

}
