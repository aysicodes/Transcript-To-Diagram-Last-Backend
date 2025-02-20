package com.example.transcripttodiagram.controller;

import com.example.transcripttodiagram.dto.SubjectDTO;
import com.example.transcripttodiagram.model.Subject;
import com.example.transcripttodiagram.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping
    public ResponseEntity<List<SubjectDTO>> getAllSubjects() {
        try {
            List<SubjectDTO> subjects = subjectService.getAllSubjects()
                    .stream()
                    .map(SubjectDTO::from)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(subjects);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<Subject> getSubjectByName(@PathVariable String name) {
        return subjectService.getSubjectByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Subject> createSubject(@RequestBody Subject subject) {
        return ResponseEntity.ok(subjectService.saveSubject(subject));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }
}



//import com.example.transcripttodiagram.model.Subject;
//import com.example.transcripttodiagram.service.SubjectService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/subjects")
//public class SubjectController {
//    private final SubjectService subjectService;
//
//    public SubjectController(SubjectService subjectService) {
//        this.subjectService = subjectService;
//    }
//
//    @GetMapping
//    public List<Subject> getAllSubjects() {
//        return subjectService.getAllSubjects();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Subject> getSubjectById(@PathVariable int id) {
//        Optional<Subject> subject = subjectService.getSubjectById(id);
//        return subject.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    @PostMapping
//    public Subject createSubject(@RequestBody Subject subject) {
//        return subjectService.createSubject(subject);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Subject> updateSubject(@PathVariable int id, @RequestBody Subject subjectDetails) {
//        try {
//            return ResponseEntity.ok(subjectService.updateSubject(id, subjectDetails));
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteSubject(@PathVariable int id) {
//        subjectService.deleteSubject(id);
//        return ResponseEntity.noContent().build();
//    }
//}
