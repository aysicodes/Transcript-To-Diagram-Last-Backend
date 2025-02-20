package com.example.transcripttodiagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.example.transcripttodiagram.repository")
public class TranscriptToDiagramApplication {

    public static void main(String[] args) {
        SpringApplication.run(TranscriptToDiagramApplication.class, args);
    }

}
