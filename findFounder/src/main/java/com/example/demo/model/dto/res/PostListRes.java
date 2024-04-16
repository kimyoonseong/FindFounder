package com.example.demo.model.dto.res;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.demo.model.dto.PostDetailDto;
import com.example.demo.model.entity.Post;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PostListRes {
	private Page<PostDetailDto>  posts;
}
