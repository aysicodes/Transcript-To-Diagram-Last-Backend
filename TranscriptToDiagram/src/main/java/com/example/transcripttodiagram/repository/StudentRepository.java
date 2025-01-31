package com.example.transcripttodiagram.repository;

import com.example.transcripttodiagram.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByEmail(String email);
    List<Student> findByLastLoginBefore(LocalDateTime date);
}