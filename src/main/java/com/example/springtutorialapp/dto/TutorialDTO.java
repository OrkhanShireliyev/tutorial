package com.example.springtutorialapp.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TutorialDTO {
    private String title;
    private String description;
    private boolean published;
    private String imageUrl;
}
