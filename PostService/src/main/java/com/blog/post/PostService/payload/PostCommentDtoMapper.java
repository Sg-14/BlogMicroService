package com.blog.post.PostService.payload;

import java.util.List;

public class PostCommentDtoMapper {

    public static PostCommentDto postCommentDtoMapper(
            PostDto postDto, List<CommentDto> commentDtos, String categoryName){
        PostCommentDto postCommentDto = new PostCommentDto();

        postCommentDto.setId(postDto.getId());
        postCommentDto.setTitle(postDto.getTitle());
        postCommentDto.setDescription(postDto.getDescription());
        postCommentDto.setContent(postDto.getContent());
        postCommentDto.setCategoryName(categoryName);

        postCommentDto.setComments(commentDtos);

        return postCommentDto;
    }

}
