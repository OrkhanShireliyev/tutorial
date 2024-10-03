package com.example.springtutorialapp.service.impl;

import com.amazonaws.services.s3.model.S3Object;
import com.example.springtutorialapp.dto.TutorialDTO;
import com.example.springtutorialapp.exception.AlreadyExistException;
import com.example.springtutorialapp.exception.NotFoundException;
import com.example.springtutorialapp.model.Tutorial;
import com.example.springtutorialapp.repository.TutorialRepositoryJpa;
import com.example.springtutorialapp.service.S3Service;
import com.example.springtutorialapp.service.inter.TutorialServiceInter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TutorialServiceJpa implements TutorialServiceInter {

    private final TutorialRepositoryJpa tutorialRepositoryJpa;

    private final S3Service s3Service;

    public TutorialServiceJpa(TutorialRepositoryJpa tutorialRepositoryJpa, S3Service s3Service) {
        this.tutorialRepositoryJpa = tutorialRepositoryJpa;
        this.s3Service = s3Service;
    }

    public ResponseEntity<String> saveWithImage(TutorialDTO tutorialDTO, MultipartFile multipartFile){

        List<Tutorial> tutorials=tutorialRepositoryJpa.findAll();

        boolean isExistTitle=tutorials.stream().anyMatch(tutorial -> tutorial.getTitle().equals(tutorialDTO.getTitle()));

        boolean isExistTDescription=tutorials.stream().anyMatch(tutorial -> tutorial.getDescription().equals(tutorialDTO.getDescription()));

        boolean isExistTutorial=isExistTitle&&isExistTDescription;
//        System.out.println(isExistTitle);
//        System.out.println(isExistTDescription);
//        System.out.println(isExistTutorial);

        if (isExistTutorial==true){
            throw new AlreadyExistException("Already exist tutorial with title="+tutorialDTO.getTitle()+", description="+tutorialDTO.getDescription());
        }

        try {
            String imageUrl=s3Service.uploadFile(multipartFile);

            Tutorial tutorial = Tutorial.builder()
                    .title(tutorialDTO.getTitle())
                    .description(tutorialDTO.getDescription())
                    .published(tutorialDTO.isPublished())
                    .imageUrl(imageUrl)
                    .build();

            tutorialRepositoryJpa.save(tutorial);
            log.info("Successfully created {}",tutorial);
            return new ResponseEntity<>("Successfully created!", HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public S3Object downloadFile(String keyName){
        return s3Service.downloadFile(keyName);
    }

    public ResponseEntity<byte[]> downloadImage(Long id){
        return s3Service.downloadImage(id);
    }
    @Override
    public ResponseEntity<String> saveTutorial(TutorialDTO tutorialDTO) {

        List<Tutorial> tutorials=tutorialRepositoryJpa.findAll();

        boolean isExistTitle=tutorials.stream().anyMatch(tutorial -> tutorial.getTitle().equals(tutorialDTO.getTitle()));

        boolean isExistTDescription=tutorials.stream().anyMatch(tutorial -> tutorial.getDescription().equals(tutorialDTO.getDescription()));

        boolean isExistTutorial=isExistTitle&&isExistTDescription;
        System.out.println(isExistTitle);
        System.out.println(isExistTDescription);
        System.out.println(isExistTutorial);

        if (isExistTutorial==true){
            throw new AlreadyExistException("Already exist tutorial with title="+tutorialDTO.getTitle()+", description="+tutorialDTO.getDescription());
        }

        try {
            Tutorial tutorial = Tutorial.builder()
                    .title(tutorialDTO.getTitle())
                    .description(tutorialDTO.getDescription())
                    .published(tutorialDTO.isPublished())
                    .build();

            tutorialRepositoryJpa.save(tutorial);
            log.info("Successfully created {}",tutorial);
            return new ResponseEntity<>("Successfully created!", HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> updateTutorial(Long id, TutorialDTO tutorialDTO) {
        Tutorial tutorialByid=tutorialRepositoryJpa.findById(id).get();

        if (tutorialByid==null){
            throw new NotFoundException("Not found tutorial with id="+id);
        }

        tutorialByid.setTitle(tutorialDTO.getTitle());
        tutorialByid.setDescription(tutorialDTO.getDescription());
        tutorialByid.setPublished(tutorialDTO.isPublished());
        tutorialByid.setImageUrl(tutorialDTO.getImageUrl());

        tutorialRepositoryJpa.save(tutorialByid);

        if (tutorialByid!=null){
            log.info("Successfully found{}",tutorialDTO);
            return new ResponseEntity<>("Successfully updated{}",HttpStatus.OK);
        }else {
            log.error("Not found tutorial!");
            return new ResponseEntity<>("Not found tutorial!", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<TutorialDTO> getTutorialById(Long id) {
        Tutorial tutorialByid=tutorialRepositoryJpa.findById(id).get();

        if (tutorialByid==null){
            throw new NotFoundException("Not found tutorial with id="+id);
        }

        TutorialDTO tutorialDTO=TutorialDTO.builder()
                .title(tutorialByid.getTitle())
                .description(tutorialByid.getDescription())
                .published(tutorialByid.isPublished())
                .imageUrl(tutorialByid.getImageUrl())
                .build();

        if (tutorialDTO!=null){
            log.info("Successfully found{}",tutorialDTO);
            return new ResponseEntity<>(tutorialDTO,HttpStatus.OK);
        }else {
            log.error("Not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> deleteTutorial(Long id) {
        try {
            Tutorial tutorialByid=tutorialRepositoryJpa.findById(id).get();

            if (tutorialByid==null){
                throw new NotFoundException("Not found tutorial with id="+id);
            }

            tutorialRepositoryJpa.deleteById(id);

            log.info("Successfully deleted");
            return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
        }catch (Exception e){
            log.error("Can't deleted");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<TutorialDTO>> getAllTutorials(String title) {
        List<TutorialDTO> tutorialDTOS = new ArrayList<>();
        TutorialDTO tutorialDTO;

        if (title == null) {
            List<Tutorial> tutorials = tutorialRepositoryJpa.findAll();
            if (tutorials==null){
                throw new NotFoundException("Not found tutorials!");
            }
            for (Tutorial tutorial : tutorials) {
                tutorialDTO = TutorialDTO.builder()
                        .title(tutorial.getTitle())
                        .description(tutorial.getDescription())
                        .published(tutorial.isPublished())
                        .imageUrl(tutorial.getImageUrl())
                        .build();

                tutorialDTOS.add(tutorialDTO);
            }
        } else if (title != null) {
            List<Tutorial> tutorials = tutorialRepositoryJpa.findAll().stream().filter(tutorial -> tutorial.getTitle().equals(title)).collect(Collectors.toList());
            if (tutorials==null){
                throw new NotFoundException("Not found tutorials!");
            }
            for (Tutorial tutorial : tutorials) {
                tutorialDTO = TutorialDTO.builder()
                        .title(tutorial.getTitle())
                        .description(tutorial.getDescription())
                        .published(tutorial.isPublished())
                        .imageUrl(tutorial.getImageUrl())
                        .build();

                tutorialDTOS.add(tutorialDTO);
            }
        }
            if (tutorialDTOS != null) {
                log.info("Successfully retrieved{}", tutorialDTOS);
                return new ResponseEntity<>(tutorialDTOS, HttpStatus.OK);
            } else {
                log.error("Not found!");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

    @Override
    public ResponseEntity<List<TutorialDTO>> findByPublished(boolean published) {
        List<Tutorial> tutorials=tutorialRepositoryJpa.findByPublished(published);

        if (tutorials==null){
            throw new NotFoundException("Not found tutorials!");
        }
           List<TutorialDTO> tutorialDTOS=new ArrayList<>();
           TutorialDTO tutorialDTO;

           for (Tutorial tutorial:tutorials){
               tutorialDTO=TutorialDTO.builder()
                       .title(tutorial.getTitle())
                       .description(tutorial.getDescription())
                       .published(tutorial.isPublished())
                       .imageUrl(tutorial.getImageUrl())
                       .build();
               tutorialDTOS.add(tutorialDTO);
           }

           if (tutorialDTOS!=null){
               log.info("Successfully retrieved{}", tutorialDTOS);
               return new ResponseEntity<>(tutorialDTOS,HttpStatus.OK);
           }else {
               log.error("Not found!");
               return new ResponseEntity<>(HttpStatus.NOT_FOUND);
           }
    }

    @Override
    public ResponseEntity<List<TutorialDTO>> findByTitleContaining(String title) {
        List<Tutorial> tutorials=tutorialRepositoryJpa.findByTitleContaining(title);
        if (tutorials==null){
            throw new NotFoundException("Not found tutorials!");
        }
        List<TutorialDTO> tutorialDTOS=new ArrayList<>();
        TutorialDTO tutorialDTO;

        for (Tutorial tutorial:tutorials){
            tutorialDTO=TutorialDTO.builder()
                    .title(tutorial.getTitle())
                    .description(tutorial.getDescription())
                    .published(tutorial.isPublished())
                    .imageUrl(tutorial.getImageUrl())
                    .imageUrl(tutorial.getImageUrl())
                    .build();
            tutorialDTOS.add(tutorialDTO);
        }

        if (tutorialDTOS!=null){
            log.info("Successfully retrieved{}", tutorialDTOS);
            return new ResponseEntity<>(tutorialDTOS,HttpStatus.OK);
        }else {
            log.error("Not found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> deleteAll() {
        try{
            List<Tutorial> tutorials = tutorialRepositoryJpa.findAll();
            if (tutorials==null){
                throw new NotFoundException("Not found tutorials!");
            }
            tutorialRepositoryJpa.deleteAll();
            return new ResponseEntity<>("Successfully deleted!",HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Can't deleted!",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
