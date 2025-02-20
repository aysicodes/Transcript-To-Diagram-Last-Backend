package com.example.transcripttodiagram.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "skill_groups")
public class SkillGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    @CollectionTable(name = "group_keywords", joinColumns = @JoinColumn(name = "group_id", nullable = false))
    private List<String> keywords;
}