//package com.example.transcripttodiagram.model;
//
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//@Entity
//@Table(name = "common_skill_mappings")
//@Getter @Setter
//public class CommonSkillMapping {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "skill_group_id")
//    private SkillGroup skillGroup;
//
//    @ManyToOne
//    @JoinColumn(name = "skill_id")
//    private Skill skill;
//}
