package com.blog.post.PostService.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseNoComments {
    private List<PostDto> content;
    private int pageNo;
    private int pageSize;
    private boolean last;
    private long totalContent;
    private int totalPages;
}
