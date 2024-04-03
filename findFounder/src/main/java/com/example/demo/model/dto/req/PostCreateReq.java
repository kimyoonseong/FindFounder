package com.example.demo.model.dto.req;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PostCreateReq {
	private String postTitle;
	private String postContent;
	private int cusCode;
	private int postView;
}
