package org.petmarket.blog.dto.posts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.petmarket.blog.dto.category.BlogPostCategoryResponseDto;
import org.petmarket.blog.dto.comment.BlogPostCommentResponse;
import org.petmarket.blog.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BlogPostResponseDto {
    @Schema(example = "1", description = "Post Id")
    private Long id;
    @Schema(example = "ua", description = "Код перекладу")
    @JsonProperty("lang_code")
    private String langCode;
    @Schema(example = "Догляд за вівчаркою", description = "Назва блог посту")
    private String title;
    @Schema(example = "Вівчарок треба мити і вичісувати", description = "Текст блог посту")
    private String description;
    @Schema(example = "Вівчарок треба мити і вичісувати...", description = "Текст блог посту до 500 символів")
    @JsonProperty("short_text")
    private String shortText;
    @Schema(example = "3", description = "Time to read")
    @JsonProperty("reading_minutes")
    private int readingMinutes;
    private Post.Status status;
    @Schema(example = "admin@admin.com", description = "User email")
    @JsonProperty("user_name")
    private String userName;
    @Schema(example = "Bob", description = "User Name")
    @JsonProperty("first_name")
    private String firstName;
    @Schema(example = "Alison", description = "User last name")
    @JsonProperty("last_name")
    private String lastName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updated;
    @Schema(example = "/dogliad-za-vivcharkou", description = "Код перекладу")
    private String alias;
    private List<BlogPostCategoryResponseDto> categories;
    private List<BlogPostCommentResponse> comments;

}
