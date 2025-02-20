package com.example.transcripttodiagram.service;

import com.example.transcripttodiagram.model.SkillGroup;
import com.example.transcripttodiagram.repository.SkillGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SkillGrouper {
    private final SkillGroupRepository skillGroupRepository;

    public String resolveGroup(String skillName) {
        String lowerName = skillName.toLowerCase();
        Optional<SkillGroup> group = skillGroupRepository.findByKeywordsContainingIgnoreCase(lowerName);

        return group.map(SkillGroup::getName)
                .orElseGet(() -> createDefaultGroup(skillName));
    }



    private String createDefaultGroup(String skillName) {
        SkillGroup newGroup = new SkillGroup();
        newGroup.setName(skillName);
        newGroup.setKeywords(List.of(skillName.trim().toLowerCase())); // исправлено
        skillGroupRepository.save(newGroup);
        return skillName;
    }

}