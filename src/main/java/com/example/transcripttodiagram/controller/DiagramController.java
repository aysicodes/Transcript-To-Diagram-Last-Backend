package com.example.transcripttodiagram.controller;


import com.example.transcripttodiagram.model.Student;
import com.example.transcripttodiagram.service.DiagramExportService;
import com.example.transcripttodiagram.service.StudentService;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/api/diagram")
@RequiredArgsConstructor
public class DiagramController {

    private final DiagramExportService diagramExportService;
    private final StudentService studentService;

    @GetMapping("/export")
    public ResponseEntity<Resource> exportDiagram(
            @RequestParam String format,
            @RequestParam float quality,
            @RequestParam int width,
            @AuthenticationPrincipal UserDetails userDetails // Добавляем информацию о пользователе
    ) throws IOException {

        Student student = studentService.findByEmail(userDetails.getUsername());
        if (student == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        byte[] data = diagramExportService.exportDiagram(format, quality, width, student.getId());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=diagram." + format)
                .contentType(MediaType.parseMediaType("image/" + format))
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }
}