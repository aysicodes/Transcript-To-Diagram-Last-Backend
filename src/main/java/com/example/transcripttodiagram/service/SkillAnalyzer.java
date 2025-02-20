package com.example.transcripttodiagram.service;

import com.example.transcripttodiagram.model.SkillType;
import com.example.transcripttodiagram.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkillAnalyzer {
    private final SkillRepository skillRepository;

    public SkillType determineSkillType(Long skillId) {
        int usageCount = skillRepository.countSubjectsBySkillId(skillId);
        return usageCount > 1 ? SkillType.COMMON : SkillType.SINGLE;
    }
}

