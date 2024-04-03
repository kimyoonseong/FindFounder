package com.example.demo.model.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CommonRes {
	private int code;
	private String msg;
}
