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
	
	private int cus_code;
	private String cus_id;
	private String cus_pw;
	private String cus_name;
	private int cus_cupons;
	private String cus_pw_answer;	
	private Boolean cus_isconsult;
	private String cus_email;
	
	public Customer toEntity() {
		Customer customer = Customer.builder()
				.cus_code(cus_code)
				.cus_id(cus_id)
				.cus_pw(cus_pw)
				.cus_name(cus_name)
				.cus_cupons(cus_cupons)
				.cus_pw_answer(cus_pw_answer)
				.cus_isconsult(cus_isconsult)
				.cus_email(cus_email)
				.build();
		return customer;
	}

	
	

}
