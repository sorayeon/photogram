package com.cos.photogramstart.web.dto.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CommentDto {
    @Size(min = 2, max = 100)
    @NotBlank
    private String content;
    private int imageId;
}
