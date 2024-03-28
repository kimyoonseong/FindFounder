package com.example.demo.model.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;

import com.example.demo.model.dto.PostDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int post_id;
	
	@Column(nullable = false, length = 32)
	private String post_title;
	@Column(nullable = false, length = 300)
	private String post_content;
	@CreatedDate
	private Date post_date;
	@Column(columnDefinition = "INT DEFAULT 0")
	private int post_like;
	@Column(columnDefinition = "INT DEFAULT 0")
	private int post_dislike;
	private int post_views;
	
	
	@ManyToOne
	@JoinColumn(name = "cus_code")
	private Customer customer;
	
	
	@OneToMany(mappedBy = "post")
	private List<Comment> comments;
	
	
	public PostDto toDto() {
		PostDto postDto = new PostDto();
		postDto.setPost_id(this.getPost_id());
		postDto.setPost_title(this.getPost_title());
		postDto.setPost_content(this.getPost_content());
		postDto.setPost_date(this.getPost_date());
		postDto.setPost_like(this.getPost_like());
		postDto.setPost_dislike(this.getPost_dislike());
		postDto.setPost_views(this.getPost_views());
		return postDto;
	}
	
	
	
}
