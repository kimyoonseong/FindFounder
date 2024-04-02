package com.example.demo.model.dto;

import org.hibernate.annotations.ColumnDefault;

import com.example.demo.model.entity.Customer;
import com.example.demo.model.entity.Question;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@AllArgsConstructor // @Builder를 위해 추가
@NoArgsConstructor(access = AccessLevel.PRIVATE) // @RequestBody를 위해 추가
public class CustomerDto {
	
	private int cusCode;
	private String cusId;
	private String cusPw;
	private String cusName;
	private int cusCupons;
	private String cusPwAnswer;	
	private Boolean cusIsConsult;
	private String cusEmail;
	
	public Customer toEntity() {
		Customer customer = Customer.builder()
				.cusCode(this.getCusCode())
				.cusId(this.getCusId())
				.cusPw(this.getCusPw())
				.cusName(this.getCusName())
				.cusCupons(this.getCusCupons())
				.cusPwAnswer(this.getCusPwAnswer())
				.cusIsConsult(this.getCusIsConsult())
				.cusEmail(this.getCusEmail())
				.build();
		return customer;
	}
}
