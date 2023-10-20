package org.petmarket.blog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.petmarket.blog.dto.comment.BlogPostCommentResponse;
import org.petmarket.blog.entity.BlogComment;
import org.petmarket.config.MapperConfig;

@Mapper(config = MapperConfig.class)
public interface BlogCommentMapper {

    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "postId", source = "post.id")
    BlogPostCommentResponse toDto(BlogComment blogComment);

}
