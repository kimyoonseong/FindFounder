package com.example.demo.model.dto.req;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CustomerFindPwReq{
	private String cusId;
	private int cusQuestionId;
	private String cusPwAnswer;
}
