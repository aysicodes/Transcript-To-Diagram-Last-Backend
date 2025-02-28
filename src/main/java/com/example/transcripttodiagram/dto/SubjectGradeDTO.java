package com.example.transcripttodiagram.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SubjectGradeDTO {
    private String subjectName;
    private Integer score;

    public SubjectGradeDTO(@JsonProperty("subjectName") String subjectName, @JsonProperty("score") Integer score) {
        this.subjectName = subjectName;
        this.score = score;
    }
}
