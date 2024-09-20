package com.example.springtutorialapp.service.inter;

import com.example.springtutorialapp.dto.TutorialDTO;
import com.example.springtutorialapp.model.Tutorial;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TutorialServiceInter {

    ResponseEntity<String> saveTutorial(TutorialDTO tutorialDTO);

    ResponseEntity<String> updateTutorial(Long id,TutorialDTO tutorialDTO);

    ResponseEntity<TutorialDTO> getTutorialById(Long id);

    ResponseEntity<String> deleteTutorial(Long id);

    ResponseEntity<List<TutorialDTO>> getAllTutorials(String title);

    ResponseEntity<List<TutorialDTO>> findByPublished(boolean published);

    ResponseEntity<List<TutorialDTO>> findByTitleContaining(String title);

    ResponseEntity<String> deleteAll();

}
