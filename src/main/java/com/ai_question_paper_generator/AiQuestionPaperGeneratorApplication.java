package com.ai_question_paper_generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class AiQuestionPaperGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiQuestionPaperGeneratorApplication.class, args);
	}

}
