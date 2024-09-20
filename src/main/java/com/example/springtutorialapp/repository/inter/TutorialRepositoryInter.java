package com.example.springtutorialapp.repository.inter;

import com.example.springtutorialapp.model.Tutorial;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TutorialRepositoryInter {

    int save(Tutorial book);

    int update(Tutorial book);

    Tutorial findById(Long id);

    int deleteById(Long id);

    List<Tutorial> findAll();

    List<Tutorial> findByPublished(boolean published);

    List<Tutorial> findByTitleContaining(String title);

    int deleteAll();

}
