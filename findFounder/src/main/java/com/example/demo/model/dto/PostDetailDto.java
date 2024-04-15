package com.example.demo.model.dto;


import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostDetailDto {

	private String postTitle;
	private int postViews;
	private int postLike;
	private String writer;
	private String postDate;
}
