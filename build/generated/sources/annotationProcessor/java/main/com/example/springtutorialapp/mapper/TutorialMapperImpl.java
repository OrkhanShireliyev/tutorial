package com.example.springtutorialapp.mapper;

import com.example.springtutorialapp.dto.TutorialDTO;
import com.example.springtutorialapp.model.Tutorial;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-02T23:22:25+0400",
    comments = "version: 1.6.2, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.7.jar, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class TutorialMapperImpl implements TutorialMapper {

    @Override
    public Tutorial tutorialDtoToTutorial(TutorialDTO tutorialDTO) {
        if ( tutorialDTO == null ) {
            return null;
        }

        Tutorial.TutorialBuilder tutorial = Tutorial.builder();

        tutorial.title( tutorialDTO.getTitle() );
        tutorial.description( tutorialDTO.getDescription() );
        tutorial.published( tutorialDTO.isPublished() );
        tutorial.imageUrl( tutorialDTO.getImageUrl() );

        return tutorial.build();
    }

    @Override
    public TutorialDTO tutorialToTutorialDto(Tutorial tutorial) {
        if ( tutorial == null ) {
            return null;
        }

        TutorialDTO.TutorialDTOBuilder tutorialDTO = TutorialDTO.builder();

        tutorialDTO.title( tutorial.getTitle() );
        tutorialDTO.description( tutorial.getDescription() );
        tutorialDTO.published( tutorial.isPublished() );
        tutorialDTO.imageUrl( tutorial.getImageUrl() );

        return tutorialDTO.build();
    }
}
