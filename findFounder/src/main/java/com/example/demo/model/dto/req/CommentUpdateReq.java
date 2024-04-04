package com.example.demo.model.dto.req;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CommentUpdateReq {

	int cusCode;
	String commentContent;
	int postId;
	int commentId;
}
