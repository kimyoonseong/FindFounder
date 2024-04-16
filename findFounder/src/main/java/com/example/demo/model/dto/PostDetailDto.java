package com.example.demo.model.dto;


import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class PostDetailDto {
	private int postId;
	private String postTitle;
	private String postContent;
	private int postViews;
	private int postLike;
	private int postDislike;
	private String writer;
	private String postDate;
}
