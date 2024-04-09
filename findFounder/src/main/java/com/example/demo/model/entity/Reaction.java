package com.example.demo.model.entity;



import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//reactionId(PK), postId(FK), reaction(0, 1 like, 2 dislike), cusCode(FK)
// 좋아요 누르기, 좋아요 취소, 싫어요 누르기, 싫어요 취소
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Reaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int reactionId;
	
	@Column(nullable = false)
	private boolean reaction;
	
	
	@ManyToOne
	@JoinColumn(name = "cusCode")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name = "postId")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Post post;
	
	

}
