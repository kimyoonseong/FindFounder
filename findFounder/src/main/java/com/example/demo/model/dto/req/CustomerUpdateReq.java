package com.example.demo.model.dto.req;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CustomerUpdateReq {
	 private String cusPw;
	 private String cusName;
	 private String cusEmail;
	 private int cusQuestionId;
}
