package com.example.transcripttodiagram.repository;

import com.example.transcripttodiagram.model.SkillGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillGroupRepository extends JpaRepository<SkillGroup, Long> {
    Optional<SkillGroup> findByKeywordsContainingIgnoreCase(String keyword);

}