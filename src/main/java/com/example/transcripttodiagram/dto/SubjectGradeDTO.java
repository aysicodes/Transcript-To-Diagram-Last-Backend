package com.example.transcripttodiagram.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SubjectGradeDTO {
    private String subjectName;
    private Integer score;

    public SubjectGradeDTO(@JsonProperty("subjectName") String subjectName, @JsonProperty("score") Integer score) {
        this.subjectName = subjectName;
        this.score = score;
    }
}
//@Getter
//@Setter
//public class SubjectGradeDTO {
//    private final String subject;
//    private final double grade;
//
//    @JsonCreator
//    public SubjectGradeDTO(@JsonProperty("subject") String subjectName,
//                           @JsonProperty("grade") double grade) {
//        this.subject = subjectName;
//        this.grade = grade;
//    }
//
//
//}