package com.example.demo.model.dto;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CommentDetailDto {
	private int commentId;
    private String commentContent;
    private String commentDate;
    private String writer;
}
