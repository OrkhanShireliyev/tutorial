package com.example.springtutorialapp.controller;

import com.amazonaws.services.s3.model.S3Object;
import com.example.springtutorialapp.dto.TutorialDTO;
import com.example.springtutorialapp.service.S3Service;
import com.example.springtutorialapp.service.impl.TutorialServiceJpa;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/tutorial")
@Tag(name = "Tutorial Controller", description = "Operations related to tutorial management")
public class TutorialController {

    private final TutorialServiceJpa tutorialServiceInter;
    private final S3Service s3Service;

    public TutorialController(TutorialServiceJpa tutorialServiceInter, S3Service s3Service) {
        this.tutorialServiceInter = tutorialServiceInter;
        this.s3Service = s3Service;
    }

    @Operation(summary = "Save tutorial with image", description = "Fill tutorial information with image and save it!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully saved!"),
            @ApiResponse(responseCode = "500", description = "Can't save tutorial!")
    })
    @PostMapping(value = "/saveWithImage",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> saveTutorialWithImage(@RequestParam String title,
                                                        @RequestParam String description,
                                                        @RequestParam boolean published,
                                                        @RequestParam("image") MultipartFile multipartFile) throws IOException {
        String imageUrl=s3Service.uploadFile(multipartFile);
        TutorialDTO tutorialDTO=TutorialDTO.builder()
                .title(title)
                .description(description)
                .published(published)
                .imageUrl(imageUrl)
                .build();

        return tutorialServiceInter.saveWithImage(tutorialDTO,multipartFile);
    }

    @Operation(summary = "Download image file", description = "Download image!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved!"),
            @ApiResponse(responseCode = "500", description = "Can't save tutorial!")
    })
    @GetMapping("/downloadFile/{keyName}")
    public ResponseEntity<S3Object> downloadFile(@PathVariable String keyName){
        S3Object image=tutorialServiceInter.downloadFile(keyName);
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    @Operation(summary = "Download image", description = "Download image!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved!"),
            @ApiResponse(responseCode = "500", description = "Can't save tutorial!")
    })
    @GetMapping("/downloadImage/{id}")
    public ResponseEntity<ResponseEntity<byte[]>> downloadImage(@PathVariable Long id){
        ResponseEntity<byte[]> image=tutorialServiceInter.downloadImage(id);
        return new ResponseEntity<>(image,HttpStatus.OK);
    }

    @Operation(summary = "Save tutorial", description = "Fill tutorial information and save it!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully saved!"),
            @ApiResponse(responseCode = "500", description = "Can't save tutorial!")
    })
    @PostMapping("/save")
   public ResponseEntity<String> saveTutorial(@RequestBody TutorialDTO tutorialDTO){
        return tutorialServiceInter.saveTutorial(tutorialDTO);
   }

    @Operation(summary = "Update tutorial", description = "Fill tutorial for change tutorial's info and update it!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated!"),
            @ApiResponse(responseCode = "500", description = "Can't update tutorial!")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateTutorial(@PathVariable Long id, @RequestBody TutorialDTO tutorialDTO){
       return tutorialServiceInter.updateTutorial(id, tutorialDTO);
    }

    @Operation(summary = "Get a tutorial by id", description = "Returns a tutorial as per the id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The tutorial was not found")
    })
    @GetMapping("/getId/{id}")
    public ResponseEntity<TutorialDTO> getTutorialById(@PathVariable Long id){
       return tutorialServiceInter.getTutorialById(id);
    }

    @Operation(summary = "Delete a tutorial by id", description = "Delete a tutorial as per the id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Can't delete tutorial!")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTutorial(@PathVariable Long id){
       return tutorialServiceInter.deleteTutorial(id);
    }

    @Operation(summary = "Get all tutorials", description = "Get all tutorials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The tutorial was not found")
    })
    @GetMapping("/tutorials")
    public ResponseEntity<List<TutorialDTO>> getAllTutorials(@RequestParam(required = false) String title){
       return tutorialServiceInter.getAllTutorials(title);
    }

    @Operation(summary = "Get a tutorial by published", description = "Returns a tutorial as per the published")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The tutorial was not found")
    })
    @GetMapping("/byPublished/{published}")
    public ResponseEntity<List<TutorialDTO>> findByPublished(@PathVariable boolean published){
       return tutorialServiceInter.findByPublished(published);
    }

    @Operation(summary = "Get a tutorial by title", description = "Returns a tutorial as per the title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The tutorial was not found")
    })
    @GetMapping("/byTitle/{title}")
    public ResponseEntity<List<TutorialDTO>> findByTitleContaining(@PathVariable String title){
       return tutorialServiceInter.findByTitleContaining(title);
    }

    @Operation(summary = "Delete all tutorials", description = "Delete all tutorials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Can't delete tutorials!")
    })
    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAll() {
       return tutorialServiceInter.deleteAll();
    }
}
