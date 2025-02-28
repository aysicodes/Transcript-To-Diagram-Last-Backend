package com.example.transcripttodiagram.service;

import com.example.transcripttodiagram.dto.SubjectGradeDTO;
import com.example.transcripttodiagram.dto.VisualizationResponse;
import com.example.transcripttodiagram.model.Student;
import com.example.transcripttodiagram.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DiagramExportService {

    private final VisualizationService visualizationService;
    private final StudentRepository studentRepository;

    public byte[] exportDiagram(String format, float quality, int width, Long studentId) throws IOException {
        List<SubjectGradeDTO> studentData = getSubjectGradeDataForStudent(studentId);
        VisualizationResponse data = visualizationService.getVisualizationData(studentData, getStudentEmailById(studentId));
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

        for (Map.Entry<String, Double> entry : data.getCommonSkills().entrySet()) {
            dataset.addValue(entry.getValue(), "Common Skills", entry.getKey());
        }

        for (Map.Entry<String, Integer> entry : data.getSingleSkills().entrySet()) {
            dataset.addValue(entry.getValue(), "Single Skills", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Competency Analysis",
                "Skills",
                "Score",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBarPainter(new StandardBarPainter()); // Убираем градиенты для четких цветов

        // Настраиваем цвета столбцов
        Paint[] colors = new Paint[]{Color.BLUE, Color.GREEN, Color.RED, Color.ORANGE, Color.CYAN};
        for (int i = 0; i < dataset.getRowCount(); i++) {
            renderer.setSeriesPaint(i, colors[i % colors.length]);
        }

        // Добавляем заголовок и увеличиваем шрифты
        chart.setTitle(new TextTitle("Competency Analysis", new Font("Arial", Font.BOLD, 16)));
        plot.getDomainAxis().setLabelFont(new Font("Arial", Font.BOLD, 14));
        plot.getRangeAxis().setLabelFont(new Font("Arial", Font.BOLD, 14));

        // Настройка подписей оси X (поворот на 45 градусов)
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickLabelFont(new Font("Arial", Font.BOLD, 12));
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        return chart;
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
