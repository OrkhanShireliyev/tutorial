package com.example.springtutorialapp.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TutorialDTO {
    private String title;
    private String description;
    private boolean published;
}
