package com.example.demo.model.dto;

import java.util.Date;

import com.example.demo.model.entity.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

	
	private int comment_id;
	private String comment_content;
	private Date comment_date;
	
	public Comment toEntity() {
		Comment comment = new Comment();
		
		comment.setComment_id(this.comment_id);
		comment.setComment_content(this.comment_content);
		comment.setComment_date(this.comment_date);
		
		return comment;
		
		
	}
}
