package com.example.demo.model.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.demo.model.dto.PostDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
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
@EntityListeners(AuditingEntityListener.class)
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int postId;
	
	@Column(nullable = false, length = 32)
	private String postTitle;
	@Column(nullable = false, length = 300)
	private String postContent;
	@CreatedDate
	private Date postDate;
	@Column(columnDefinition = "INT DEFAULT 0")
	private int postLike;
	@Column(columnDefinition = "INT DEFAULT 0")
	private int postDislike;
	private int postViews;
	
	
	@ManyToOne
	@JoinColumn(name = "cus_code")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Customer customer;
	
	
	@OneToMany(mappedBy = "post")
	private List<Comment> comments;
	
	
	public PostDto toDto() {
		PostDto postDto = new PostDto();
		postDto.setPostId(this.getPostId());
		postDto.setPostTitle(this.getPostTitle());
		postDto.setPostContent(this.getPostContent());
		postDto.setPostDate(this.getPostDate());
		postDto.setPostLike(this.getPostLike());
		postDto.setPostDislike(this.getPostDislike());
		postDto.setPostViews(this.getPostViews());
		return postDto;
	}
}
