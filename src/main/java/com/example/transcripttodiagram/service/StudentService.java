package com.example.transcripttodiagram.service;



import com.example.transcripttodiagram.model.Student;
import com.example.transcripttodiagram.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    public Student findById(Long id) {
        return studentRepository.findById(id).orElse(null); // Возвращаем null, если студент не найден
    }

    public void delete(Student student) {
        studentRepository.delete(student);
    }


    @Scheduled(cron = "${app.student.cleanup-cron}")
    @Transactional
    public void deleteInactiveStudents() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusMonths(3);
        List<Student> inactiveStudents = studentRepository.findInactiveStudents(cutoffDate);
        inactiveStudents.forEach(studentRepository::delete);
    }

    @Transactional
    public Student updateLastLogin(String email) {
        Student student = studentRepository.findByEmail(email);
        student.setLastLogin(LocalDateTime.now());
        return studentRepository.save(student);
    }
}
