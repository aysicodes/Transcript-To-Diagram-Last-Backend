package com.example.transcripttodiagram.repository;

//import com.example.transcripttodiagram.model.SingleSkills;
//import com.example.transcripttodiagram.model.Skill;
//import com.example.transcripttodiagram.model.SubjectCommonSkills;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface SkillRepository extends JpaRepository<Skill, Integer> {
//    List<Skill> countSubjectsBySkillId(int subjectId);
//}


import com.example.transcripttodiagram.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    @Query("SELECT COUNT(s) FROM Subject s JOIN s.skills skill WHERE skill.id = :skillId")
    int countSubjectsBySkillId(Long skillId);
}