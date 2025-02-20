package com.example.transcripttodiagram.service;

import com.example.transcripttodiagram.dto.SubjectGradeDTO;
import com.example.transcripttodiagram.dto.VisualizationResponse;
import com.example.transcripttodiagram.model.Skill;
import com.example.transcripttodiagram.model.SkillType;
import com.example.transcripttodiagram.model.Student;
import com.example.transcripttodiagram.model.StudentSubject;
import com.example.transcripttodiagram.model.Subject;
import com.example.transcripttodiagram.repository.SkillRepository;
import com.example.transcripttodiagram.repository.StudentRepository;
import com.example.transcripttodiagram.repository.SubjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class VisualizationService {
    private final SkillAnalyzer skillAnalyzer;
    private final SkillGrouper skillGrouper;
    private final SkillRepository skillRepository;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;

    public VisualizationResponse processSubjects(List<SubjectGradeDTO> input, String email) {
        System.out.println("Input: " + input);

        Student student = studentRepository.findByEmail(email);
        if (student == null) {
            throw new RuntimeException("Student not found");
        }
        student.getSelectedSubjects().clear();

        Set<Skill> allSkills = new HashSet<>();

        input.forEach(dto -> {
            Subject subject = subjectRepository.findByName(dto.getSubjectName())
                    .orElseThrow(() -> new RuntimeException("Subject not found: " + dto.getSubjectName()));

            StudentSubject ss = new StudentSubject();
            ss.setStudent(student);
            ss.setSubject(subject);
            ss.setScore(dto.getScore());
            student.getSelectedSubjects().add(ss);

            allSkills.addAll(subject.getSkills());
        });

        studentRepository.save(student);

        if (allSkills.isEmpty()) {
            throw new RuntimeException("No skills found for the given subjects");
        }

        Map<Skill, SkillType> skillTypes = new HashMap<>();
        Map<Skill, String> skillGroups = new HashMap<>();

        allSkills.forEach(skill -> {
            SkillType type = skillAnalyzer.determineSkillType(skill.getId());
            skillTypes.put(skill, type);

            if (type == SkillType.COMMON) {
                String group = skillGrouper.resolveGroup(skill.getName());
                if (group == null) {
                    throw new RuntimeException("Не удалось найти группу для навыка: " + skill.getName());
                }
                skillGroups.put(skill, group);
            }
        });

        Map<String, Double> commonSkills = new HashMap<>();
        Map<String, Integer> singleSkills = new HashMap<>();

        skillGroups.forEach((skill, group) -> {
            List<Integer> grades = input.stream()
                    .filter(dto -> subjectRepository.findByName(dto.getSubjectName())
                            .map(s -> s.getSkills().contains(skill))
                            .orElse(false))
                    .map(SubjectGradeDTO::getScore)
                    .collect(Collectors.toList());

            if (!grades.isEmpty()) {
                double average = grades.stream()
                        .mapToInt(Integer::intValue)
                        .average()
                        .orElse(0.0);
                commonSkills.merge(group, average, (oldVal, newVal) -> (oldVal + newVal) / 2);
            }
        });

        allSkills.stream()
                .filter(skill -> skillTypes.get(skill) == SkillType.SINGLE)
                .forEach(skill -> input.stream()
                        .filter(dto -> subjectRepository.findByName(dto.getSubjectName())
                                .map(s -> s.getSkills().contains(skill))
                                .orElse(false))
                        .findFirst()
                        .ifPresent(dto -> singleSkills.put(skill.getName(), dto.getScore())));

        System.out.println("Common Skills: " + commonSkills);
        System.out.println("Single Skills: " + singleSkills);

        return new VisualizationResponse(commonSkills, singleSkills);
    }

    public VisualizationResponse getVisualizationData(List<SubjectGradeDTO> input, String email) {
        return processSubjects(input, email);
    }


}
