package com.example.demo.model.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LoginRes {
	private int code;
	private String msg;
	private String token;
}
