package com.blog.post.PostService.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private List<PostCommentDto> content;
    private int pageNo;
    private int pageSize;
    private boolean last;
    private long totalContent;
    private int totalPages;
}
