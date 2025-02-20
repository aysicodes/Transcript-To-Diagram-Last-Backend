package com.example.transcripttodiagram.controller;

import com.example.transcripttodiagram.dto.SubjectGradeDTO;
import com.example.transcripttodiagram.service.VisualizationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/visualize")
@RequiredArgsConstructor
public class VisualizationController {
    private final VisualizationService visualizationService;

    @PostMapping
    @Transactional
    public ResponseEntity<?> visualize(
            @RequestBody List<SubjectGradeDTO> input,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            return ResponseEntity.ok(
                    visualizationService.processSubjects(input, userDetails.getUsername())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
