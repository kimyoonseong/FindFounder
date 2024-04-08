package com.example.demo.model.dto;

import com.example.demo.model.entity.Customer;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CustomerJoinDto {
	
	private String cusName;
	private String cusEmail;
	private String cusId;
	private String cusPw;
	private String cusPwAnswer;
	private int cusQuestionId;
	
}
