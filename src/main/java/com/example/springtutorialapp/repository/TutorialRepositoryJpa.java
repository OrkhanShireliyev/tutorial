package com.example.springtutorialapp.repository;

import com.example.springtutorialapp.model.Tutorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TutorialRepositoryJpa extends JpaRepository<Tutorial,Long> {
     List<Tutorial> findByPublished(boolean published);

     List<Tutorial> findByTitleContaining(String title);

}
