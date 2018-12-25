package com.bytecubed;

import com.bytecubed.models.Placement;
import com.bytecubed.parser.RavensPowerPointParser;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/playcards")
public class PlayCardController {
    @GetMapping("/card")
    public HttpEntity getPlayCard(){
        return ok("Test");
    }

    @PostMapping()
    public HttpEntity<List<Placement>> importCard(@RequestParam("file") MultipartFile file,
                                                  RedirectAttributes redirectAttributes) throws IOException {
        XMLSlideShow ppt = new XMLSlideShow(file.getInputStream());
        System.out.println( "PowerPoint with slidecount:  "+ ppt.getSlides().size());

        return ok(new RavensPowerPointParser(ppt).extractPlayerPlacements());
    }


}
