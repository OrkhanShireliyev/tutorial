package com.example.springtutorialapp.service.impl;

import com.example.springtutorialapp.dto.TutorialDTO;
import com.example.springtutorialapp.exception.NotFoundException;
import com.example.springtutorialapp.model.Tutorial;
import com.example.springtutorialapp.repository.inter.TutorialRepositoryInter;
import com.example.springtutorialapp.service.inter.TutorialServiceInter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TutorialServiceImpl implements TutorialServiceInter{

    private final TutorialRepositoryInter tutorialRepositoryInter;

    public TutorialServiceImpl(TutorialRepositoryInter tutorialRepositoryInter) {
        this.tutorialRepositoryInter = tutorialRepositoryInter;
    }
    @Override
    public ResponseEntity<String> saveTutorial(TutorialDTO tutorialDTO) {
        try {
            Tutorial tutorial=Tutorial.builder()
                    .title(tutorialDTO.getTitle())
                    .description(tutorialDTO.getDescription())
                    .published(false)
                    .build();

            tutorialRepositoryInter.save(tutorial);
            log.info(" Successfully created!: {}",tutorial);
            return new ResponseEntity<>("Successfully created!", HttpStatus.CREATED);
        }catch (Exception e){
            log.error("An error occurred while adding the tutorial!");
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> updateTutorial(Long id, TutorialDTO tutorialDTO) {
        Tutorial tutorialById = tutorialRepositoryInter.findById(id);

        if (tutorialById==null){
            new NotFoundException("Cannot find tutorial with id="+id);
        }

        if (tutorialById!=null) {

            tutorialById.setTitle(tutorialDTO.getTitle());
            tutorialById.setDescription(tutorialDTO.getDescription());
            tutorialById.setPublished(tutorialDTO.isPublished());

            tutorialRepositoryInter.update(tutorialById);
            log.info(tutorialById +" Successfully updated!");

            return new ResponseEntity<>("Successfully updated!", HttpStatus.OK);
        } else {
            log.error("Cannot find with id="+id);
            return new ResponseEntity<>("Cannot find with id="+id,HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<TutorialDTO> getTutorialById(Long id) {
        Tutorial tutorial=tutorialRepositoryInter.findById(id);

        if (tutorial==null){
            new NotFoundException("Cannot find tutorial with id="+id);
        }

        TutorialDTO tutorialDTO= TutorialDTO.builder()
                .title(tutorial.getTitle())
                .description(tutorial.getDescription())
                .published(tutorial.isPublished())
                .build();

        if (tutorialDTO!=null){
            log.info("Successfully found{}",tutorialDTO);
            return new ResponseEntity<>(tutorialDTO,HttpStatus.OK);
        }else {
            log.error("Not found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> deleteTutorial(Long id) {
        try {
            int deletingTutorial=tutorialRepositoryInter.deleteById(id);

            if (deletingTutorial==0){
                new NotFoundException("Cannot find tutorial with id="+id);
                log.error("Tutorial was not found with id "+id);
                return new ResponseEntity<>("Cannot find Tutorial with id="+id,HttpStatus.NOT_FOUND);
            }else {
                log.info("Successfully deleted tutorial with id "+id);
                return new ResponseEntity<>("Successfully deleting!",HttpStatus.OK);
            }
        }catch (Exception e){
            log.info("Cannot delete Tutorial!");
            return new ResponseEntity<>("Cannot delete Tutorial!",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<TutorialDTO>> getAllTutorials(String title) {
        try {
            List<TutorialDTO> tutorialDTOS=new ArrayList<>();

            TutorialDTO tutorialDTO;

            if (title==null){
                List<Tutorial> tutorials=tutorialRepositoryInter.findAll();

                if (tutorials.isEmpty()){
                    new NotFoundException("Don't have any data!");
                }

                for (Tutorial tutorial:tutorials) {
                    tutorialDTO= TutorialDTO.builder()
                            .title(tutorial.getTitle())
                            .description(tutorial.getDescription())
                            .published(tutorial.isPublished())
                            .build();

                    tutorialDTOS.add(tutorialDTO);
                }

            }else {

                List<Tutorial> titleContains=tutorialRepositoryInter.findByTitleContaining(title);

                if (titleContains.isEmpty()){
                    new NotFoundException("Don't have any data!");
                }

                for (Tutorial tutorial:titleContains){
                    tutorialDTO= TutorialDTO.builder()
                            .title(tutorial.getTitle())
                            .description(tutorial.getDescription())
                            .published(tutorial.isPublished()).build();

                    tutorialDTOS.add(tutorialDTO);
                }
            }

            if (tutorialDTOS==null){
                new NotFoundException("Don't have any data!");
                log.error("Don't have any data!");
                return new ResponseEntity<>(HttpStatus.valueOf("Don't have any data!".toUpperCase()).NO_CONTENT);
            }else{
                new NotFoundException("Don't have any data!");
                log.info("Successfully found Tutorial ",tutorialDTOS);
                return new ResponseEntity<>(tutorialDTOS,HttpStatus.OK);
            }
        }catch (Exception e){
            log.error("Error happened!");
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<TutorialDTO>> findByPublished(boolean published) {
        try{
            List<Tutorial> tutorialsByPublished=tutorialRepositoryInter.findByPublished(published);

            List<TutorialDTO> tutorialDTOS=new ArrayList<>();

            for (Tutorial tutorial:tutorialsByPublished){
                TutorialDTO tutorialDTO= TutorialDTO.builder()
                                .title(tutorial.getTitle())
                                .description(tutorial.getDescription())
                                .published(tutorial.isPublished()).build();

                tutorialDTOS.add(tutorialDTO);
            }

            if (tutorialDTOS.isEmpty()){
                log.error("Tutorial was not found with published type "+published);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }else {
                log.info("Successfully found Tutorial ",tutorialDTOS);
                return new ResponseEntity<>(tutorialDTOS,HttpStatus.OK);
            }
        }catch (Exception e){
            log.error("Error happened!");
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<TutorialDTO>> findByTitleContaining(String title) {
        try{
            List<Tutorial> tutorials=tutorialRepositoryInter.findByTitleContaining(title);

            List<TutorialDTO> tutorialDTOS=new ArrayList<>();

            for (Tutorial tutorial:tutorials){
                TutorialDTO tutorialDTO=TutorialDTO.builder()
                        .title(tutorial.getTitle())
                        .description(tutorial.getDescription())
                        .published(tutorial.isPublished())
                        .build();
                tutorialDTOS.add(tutorialDTO);
            }

            if (tutorials!=null){
                log.info("Successfully found Tutorial ",tutorialDTOS);
                return new ResponseEntity<>(tutorialDTOS,HttpStatus.OK);
            }else {
                log.error("Tutorial was not found with published type "+title);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            log.error("Error happened!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteAll() {
        try {
            int deleted=tutorialRepositoryInter.deleteAll();
            log.info("Deleted "+deleted+" tutorials successfully!");
            return new ResponseEntity<>("Deleted "+deleted+" tutorials successfully!",HttpStatus.OK);
        } catch (Exception e) {
            log.error("Cannot deleted tutorial!");
            return new ResponseEntity<>("Cannot deleted tutorial!",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
