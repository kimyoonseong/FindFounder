package com.example.demo.model.dto;

import com.example.demo.model.entity.Customer;
import com.example.demo.model.entity.Question;

import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@Getter 
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionDto {

	private int cus_question_id;
	private String question_content;
	
	public Question toEntity() {
		Question question = Question.builder()
				.cus_question_id(cus_question_id)
				.question_content(question_content)
				.build();
		return question;
		
	}
}
