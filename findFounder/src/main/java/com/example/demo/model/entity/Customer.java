package com.example.demo.model.entity;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private int cus_code;
	
	
	@Column(nullable = false, length = 16)
	private String cus_id;
	
	@Column(nullable = false, length = 16)
	private String cus_pw;
	
	@Column(nullable = false, length = 4)
	private String cus_name;
	
	@ColumnDefault("0")
	private int cus_cupons;
	
	@Column(nullable = false, length = 8)
	private String cus_pw_answer;
	
	private Boolean cus_isconsult;
	
	@Column(nullable = false, length = 48)
	private String cus_email;
	
	@OneToOne
	@JoinColumn(name = "cus_question_id")
	private Question question;
	
	
}

