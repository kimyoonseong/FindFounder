package com.example.demo.model.dto.req;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
//@Builder
//reactionId(PK), postId(FK), reaction(1 like, 0 dislike), cusCode(FK)
public class ReactionReq {

	private int reaction;
//	private int postId;
}
