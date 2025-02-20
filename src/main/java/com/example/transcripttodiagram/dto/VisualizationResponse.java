package com.example.transcripttodiagram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Map;

@Data
@AllArgsConstructor
public class VisualizationResponse {
    private Map<String, Double> commonSkills;
    private Map<String, Integer> singleSkills;
}