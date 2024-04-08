package com.example.demo.model.entity;

import java.util.Date;
import java.util.List;

import com.example.demo.model.dto.QuestionDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor //둘다 data에 포함되어있음

@NoArgsConstructor  // JPA 엔티티 클래스에서 기본 생성자가 필요한 경우



@Data
@Builder
@Getter
@Setter
@Entity

@ToString
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private int cusQuestionId;
	
	@Column(nullable = false, length = 10)
	private String questionContent;
	
	@OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
	private List<Customer> customers;
	
	public QuestionDto toDto() {
		return QuestionDto.builder()
				.cusQuestionId(this.getCusQuestionId())
				.questionContent(this.getQuestionContent())
				.build();
		
	}

}
