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

	private int cusQuestionId;
	private String questionContent;
	
	public Question toEntity() {
		Question question = Question.builder()
				.cusQuestionId(this.getCusQuestionId())
				.questionContent(this.getQuestionContent())
				.build();
		return question;
		
	}
}
