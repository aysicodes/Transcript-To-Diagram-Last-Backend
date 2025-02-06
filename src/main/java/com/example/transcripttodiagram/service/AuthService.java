package com.example.transcripttodiagram.service;

import com.example.transcripttodiagram.model.Student;
import com.example.transcripttodiagram.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Student student = studentRepository.findByEmail(email);
        if (student == null) {
            throw new UsernameNotFoundException("Student not found with email: " + email);
        }
        return new User(student.getEmail(), student.getPassword(), new ArrayList<>());
    }
}
