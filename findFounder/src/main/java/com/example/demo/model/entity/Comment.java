package com.example.demo.model.entity;

import java.util.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import com.example.demo.model.dto.CommentDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Comment {

	@Id
	private int comment_id;
	
	@Column(nullable = false , length = 300)
	private String comment_content;
	
	@CreatedDate
	private Date comment_date;
	
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "cus_code")
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name = "post_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Post post;
	
	
	public CommentDto toDto() {
		CommentDto commentDto = new CommentDto();
		
		commentDto.setComment_id(this.getComment_id());
		commentDto.setComment_content(this.getComment_content());
		commentDto.setComment_date(this.getComment_date());
		
		return commentDto;
		
	}
	
}