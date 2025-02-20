package com.example.transcripttodiagram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StudentDTO {
    private String email;
    private LocalDateTime lastLogin;
}

