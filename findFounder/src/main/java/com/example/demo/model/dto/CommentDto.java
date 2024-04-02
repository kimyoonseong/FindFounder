package com.example.demo.model.dto;

import java.util.Date;

import com.example.demo.model.entity.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private int commentId;
    private String commentContent;
    private Date commentDate;

    public Comment toEntity() {
        Comment comment = new Comment();

        comment.setCommentId(this.getCommentId());
        comment.setCommentContent(this.getCommentContent());
        comment.setCommentDate(this.getCommentDate());

        return comment;
    }
}
