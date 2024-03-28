package com.example.demo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@Entity
@ToString
public class Question {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private int cus_question_id;
	
	@Column(nullable = false, length = 10)
	private String question_content;
	
	@OneToOne(mappedBy = "question")
	private Customer customer;

}
