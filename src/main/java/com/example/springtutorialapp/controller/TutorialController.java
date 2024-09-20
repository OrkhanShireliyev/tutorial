package com.example.springtutorialapp.controller;

import com.example.springtutorialapp.dto.TutorialDTO;
import com.example.springtutorialapp.service.inter.TutorialServiceInter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tutorial")
public class TutorialController {

    private final TutorialServiceInter tutorialServiceInter;

    public TutorialController(TutorialServiceInter tutorialServiceInter) {
        this.tutorialServiceInter = tutorialServiceInter;
    }

    @PostMapping("/save")
   public ResponseEntity<String> saveTutorial(@RequestBody TutorialDTO tutorialDTO){
        return tutorialServiceInter.saveTutorial(tutorialDTO);
   }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateTutorial(@PathVariable Long id, @RequestBody TutorialDTO tutorialDTO){
       return tutorialServiceInter.updateTutorial(id, tutorialDTO);
    }

    @GetMapping("/getId/{id}")
    public ResponseEntity<TutorialDTO> getTutorialById(@PathVariable Long id){
       return tutorialServiceInter.getTutorialById(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTutorial(@PathVariable Long id){
       return tutorialServiceInter.deleteTutorial(id);
    }

    @GetMapping("/tutorials")
    public ResponseEntity<List<TutorialDTO>> getAllTutorials(@RequestParam(required = false) String title){
       return tutorialServiceInter.getAllTutorials(title);
    }

    @GetMapping("/byPublished/{published}")
    public ResponseEntity<List<TutorialDTO>> findByPublished(@PathVariable boolean published){
       return tutorialServiceInter.findByPublished(published);
    }

    @GetMapping("/byTitle/{title}")
    public ResponseEntity<List<TutorialDTO>> findByTitleContaining(@PathVariable String title){
       return tutorialServiceInter.findByTitleContaining(title);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAll() {
       return tutorialServiceInter.deleteAll();
    }
}
