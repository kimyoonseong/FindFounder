package com.example.demo.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LoginReq {

	private String cusId;
	private String cusPw;
}
