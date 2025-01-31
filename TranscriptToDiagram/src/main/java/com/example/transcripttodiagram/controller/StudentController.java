package com.example.transcripttodiagram.controller;


import com.example.transcripttodiagram.model.Student;
import com.example.transcripttodiagram.security.JwtUtil;
import com.example.transcripttodiagram.service.AuthService;
import com.example.transcripttodiagram.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Student student) {
        if (studentService.findByEmail(student.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        student.setPassword(passwordEncoder.encode(student.getPassword()));
        student.setLastLogin(LocalDateTime.now());
        Student savedStudent = studentService.save(student);

        UserDetails userDetails = authService.loadUserByUsername(savedStudent.getEmail());
        String token = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.get("email"),
                            credentials.get("password")
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }

        UserDetails userDetails = authService.loadUserByUsername(credentials.get("email"));
        String token = jwtUtil.generateToken(userDetails.getUsername());

        Student student = studentService.findByEmail(credentials.get("email"));
        student.setLastLogin(LocalDateTime.now());
        studentService.save(student);

        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.findAll());
    }


//    @GetMapping("/profile")
//    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String token) {
//        String email = jwtUtil.getEmailFromToken(token.replace("Bearer ", ""));
//        Student student = studentService.findByEmail(email);
//        return ResponseEntity.ok(student);
//    }

//    @PutMapping("/profile")
//    public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String token,
//                                           @RequestBody Student updatedStudent) {
//        String email = jwtUtil.getEmailFromToken(token.replace("Bearer ", ""));
//        Student student = studentService.findByEmail(email);
//
//        if (updatedStudent.getEmail() != null) {
//            student.setEmail(updatedStudent.getEmail());
//        }
//        if (updatedStudent.getPassword() != null) {
//            student.setPassword(passwordEncoder.encode(updatedStudent.getPassword()));
//        }
//
//        studentService.save(student);
//        return ResponseEntity.ok(student);
//    }
//
//    @DeleteMapping("/profile")
//    public ResponseEntity<?> deleteProfile(@RequestHeader("Authorization") String token) {
//        String email = jwtUtil.getEmailFromToken(token.replace("Bearer ", ""));
//        Student student = studentService.findByEmail(email);
//        studentService.delete(student);
//        return ResponseEntity.ok().build();
//    }

    // Получить студента по ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        Student student = studentService.findById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    // Получить студента по email
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getStudentByEmail(@PathVariable String email) {
        Student student = studentService.findByEmail(email);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    // Обновить студента по ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudentById(@PathVariable Long id, @RequestBody Student updatedStudent) {
        Student student = studentService.findById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }

        if (updatedStudent.getEmail() != null) {
            student.setEmail(updatedStudent.getEmail());
        }
        if (updatedStudent.getPassword() != null) {
            student.setPassword(passwordEncoder.encode(updatedStudent.getPassword()));
        }

        studentService.save(student);
        return ResponseEntity.ok(student);
    }

    // Обновить студента по email
    @PutMapping("/email/{email}")
    public ResponseEntity<?> updateStudentByEmail(@PathVariable String email, @RequestBody Student updatedStudent) {
        Student student = studentService.findByEmail(email);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }

        if (updatedStudent.getEmail() != null) {
            student.setEmail(updatedStudent.getEmail());
        }
        if (updatedStudent.getPassword() != null) {
            student.setPassword(passwordEncoder.encode(updatedStudent.getPassword()));
        }

        studentService.save(student);
        return ResponseEntity.ok(student);
    }

    // Удалить студента по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudentById(@PathVariable Long id) {
        Student student = studentService.findById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        studentService.delete(student);
        return ResponseEntity.ok().build();
    }

    // Удалить студента по email
    @DeleteMapping("/email/{email}")
    public ResponseEntity<?> deleteStudentByEmail(@PathVariable String email) {
        Student student = studentService.findByEmail(email);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        studentService.delete(student);
        return ResponseEntity.ok().build();
    }
}



