package org.petmarket.blog.controller;

import lombok.RequiredArgsConstructor;
import org.petmarket.blog.dto.posts.BlogPostResponseDto;
import org.petmarket.blog.service.PostService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/blog")
@RequiredArgsConstructor
public class BlogPostController {
    private final PostService postService;

    @GetMapping("/{postId}/{langCode}")
    public BlogPostResponseDto getPost(@PathVariable Long postId,
                                       @PathVariable String langCode) {
        return postService.get(postId, langCode);
    }

    @GetMapping("/{langCode}")
    public List<BlogPostResponseDto> getAll(Pageable pageable,
                                            @PathVariable String langCode) {
        return postService.getAll(pageable, langCode);
    }
}
