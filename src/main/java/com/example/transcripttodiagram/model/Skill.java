package com.example.transcripttodiagram.model;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "skills")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}