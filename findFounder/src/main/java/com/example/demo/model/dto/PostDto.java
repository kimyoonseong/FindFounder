package com.example.demo.model.dto;

import java.time.LocalDateTime;
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
	private int postId;
	private String postTitle;
	private String postContent;
	private Date postDate;
	private int postLike;
	private int postDislike;
	private int postViews;
	
	
	
	public Post toEntity() {
		Post post = new Post();
		
		post.setPostTitle(this.getPostTitle());
		post.setPostContent(this.getPostContent());
		post.setPostDate(this.getPostDate());
		post.setPostLike(this.getPostLike());
		post.setPostDislike(this.getPostDislike());
		post.setPostViews(this.getPostViews());
		
		return post;
		
	}
}
