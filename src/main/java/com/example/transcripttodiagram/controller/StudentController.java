package com.example.transcripttodiagram.controller;

import com.example.transcripttodiagram.dto.StudentDTO;
import com.example.transcripttodiagram.model.Student;
import com.example.transcripttodiagram.repository.StudentRepository;
import com.example.transcripttodiagram.security.JwtUtil;
import com.example.transcripttodiagram.service.AuthService;
import com.example.transcripttodiagram.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class StudentController {

    private final StudentService studentService;
    private final StudentRepository studentRepository;
    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Student student) {
        if (studentService.findByEmail(student.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Email already exists"));
        }

        student.setPassword(passwordEncoder.encode(student.getPassword()));
        student.setLastLogin(LocalDateTime.now());

        if (student.getSelectedSubjects() == null) {
            student.setSelectedSubjects(List.of());
        }

        Student savedStudent = studentService.save(student);
        UserDetails userDetails = authService.loadUserByUsername(savedStudent.getEmail());
        String token = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        logger.info("Login attempt for email: {}", credentials.get("email"));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.get("email"),
                            credentials.get("password")
                    )
            );
        } catch (BadCredentialsException e) {
            logger.warn("Failed login attempt for email: {}", credentials.get("email"));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }

        UserDetails userDetails = authService.loadUserByUsername(credentials.get("email"));
        String token = jwtUtil.generateToken(userDetails.getUsername());

        Student student = studentService.findByEmail(credentials.get("email"));
        student.setLastLogin(LocalDateTime.now());
        studentRepository.save(student);

        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentStudent(Authentication authentication) {
        String email = authentication.getName();
        Student student = studentService.findByEmail(email);

        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Student not found"));
        }

        StudentDTO studentDTO = new StudentDTO(student.getEmail(), student.getLastLogin());
        return ResponseEntity.ok(studentDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateStudent(Authentication authentication, @RequestBody Student updatedStudent) {
        String email = authentication.getName();
        Student student = studentService.findByEmail(email);

        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Student not found"));
        }

        if (updatedStudent.getEmail() != null && !updatedStudent.getEmail().equals(student.getEmail())) {
            student.setEmail(updatedStudent.getEmail());
        }
        if (updatedStudent.getPassword() != null && !updatedStudent.getPassword().isEmpty()) {
            student.setPassword(passwordEncoder.encode(updatedStudent.getPassword()));
        }

        studentService.save(student);
        String newToken = jwtUtil.generateToken(student.getEmail());

        return ResponseEntity.ok(Map.of("token", newToken, "email", student.getEmail()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteStudent(Authentication authentication) {
        String email = authentication.getName();
        Student student = studentService.findByEmail(email);

        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Student not found"));
        }

        studentService.delete(student);
        return ResponseEntity.ok(Map.of("message", "Student deleted successfully"));
    }

    @GetMapping("/all")
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<StudentDTO> students = studentService.findAll()
                .stream()
                .map(s -> new StudentDTO(s.getEmail(), s.getLastLogin()))
                .collect(Collectors.toList());


        return ResponseEntity.ok(students);
    }
}
