package com.example.transcripttodiagram;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest

@Transactional

public class StudentSubjectsRepositoryTest {

    @Autowired

    private StudentSubjectsRepository studentSubjectsRepository;

    @Test

    public void testSaveStudentSubject() {

        StudentSubject studentSubject = new StudentSubject();

// Установите необходимые поля

        studentSubjectsRepository.save(studentSubject);

        assertNotNull(studentSubject.getId());

    }

}
