package com.example.demo.model.dto;

import java.util.Date;

import com.example.demo.model.entity.Comment;
import com.example.demo.model.entity.Customer;
import com.example.demo.model.entity.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
	private int post_id;
	private String post_title;
	private String post_content;
	private Date post_date;
	private int post_like;
	private int post_dislike;
	private int post_views;
	
	
	public Post toEntity() {
		Post post = new Post();
		
		post.setPost_id(this.post_id);
		post.setPost_title(this.post_title);
		post.setPost_content(this.post_content);
		post.setPost_date(this.post_date);
		post.setPost_like(this.post_like);
		post.setPost_dislike(this.post_dislike);
		post.setPost_views(this.post_views);
		
		return post;
		
	}
}
