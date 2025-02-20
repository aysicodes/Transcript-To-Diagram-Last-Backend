package com.example.transcripttodiagram.service;

import com.example.transcripttodiagram.dto.SubjectGradeDTO;
import com.example.transcripttodiagram.dto.VisualizationResponse;
import com.example.transcripttodiagram.model.Student;
import com.example.transcripttodiagram.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiagramExportService {

    private final VisualizationService visualizationService;
    private final StudentRepository studentRepository;

    public byte[] exportDiagram(String format, float quality, int width, Long studentId) throws IOException {
        // Получаем список SubjectGradeDTO для конкретного студента по studentId
        List<SubjectGradeDTO> studentData = getSubjectGradeDataForStudent(studentId);

        // Получаем данные для визуализации
        VisualizationResponse data = visualizationService.getVisualizationData(studentData, getStudentEmailById(studentId));

        // Генерируем изображение
        JFreeChart chart = createChart(data);
        return saveChartAsImage(chart, format, width, (int)(width * 0.6), quality);
    }

    private List<SubjectGradeDTO> getSubjectGradeDataForStudent(Long studentId) {
        return studentRepository.findSubjectGradesById(studentId);
    }

    private String getStudentEmailById(Long studentId) {
        return studentRepository.findById(studentId)
                .map(Student::getEmail)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
    }

    private JFreeChart createChart(VisualizationResponse data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        data.getCommonSkills().forEach((skill, score) ->
                dataset.addValue(score, "Common Skills", skill));

        data.getSingleSkills().forEach((skill, score) ->
                dataset.addValue(score, "Single Skills", skill));

        return ChartFactory.createBarChart(
                "Competency Analysis",
                "Skills",
                "Score",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }

    private byte[] saveChartAsImage(JFreeChart chart, String format,
                                    int width, int height, float quality) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();

        chart.draw(g2, new Rectangle2D.Double(0, 0, width, height));
        g2.dispose();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, format, baos);
            return baos.toByteArray();
        }
    }
}
