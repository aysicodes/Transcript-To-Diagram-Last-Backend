package com.example.transcripttodiagram.dto;

import com.example.transcripttodiagram.model.Subject;
import lombok.Data;

@Data
public class SubjectDTO {
    private Long id;
    private String name;

    public static SubjectDTO from(Subject subject) {
        SubjectDTO dto = new SubjectDTO();
        dto.setId(subject.getId());
        dto.setName(subject.getName());
        return dto;
    }
}