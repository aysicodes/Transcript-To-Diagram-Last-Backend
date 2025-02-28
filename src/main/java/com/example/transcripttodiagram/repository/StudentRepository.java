package com.example.transcripttodiagram.repository;

import com.example.transcripttodiagram.dto.SubjectGradeDTO;
import com.example.transcripttodiagram.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);  // ✅ Вернет Optional


    @Query("SELECT s FROM Student s WHERE s.lastLogin < :cutoffDate")
    List<Student> findInactiveStudents(LocalDateTime cutoffDate);

    @Query("SELECT new com.example.transcripttodiagram.dto.SubjectGradeDTO(" +
            "ss.subject.name, " +
            "ss.score) " +
            "FROM StudentSubject ss " +
            "WHERE ss.student.id = :studentId")
    List<SubjectGradeDTO> findSubjectGradesById(Long studentId);
}


//public interface StudentRepository extends JpaRepository<Student, Long> {
//    Student findByEmail(String email);
//    List<Student> findByLastLoginBefore(LocalDateTime date);
//    List<SubjectGradeDTO> findSubjectGradesById(Long id);
//}