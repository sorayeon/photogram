package com.cos.photogramstart.web.dto.subscribe;

import lombok.Data;

@Data
public class SubscribeDto {
    private int id;
    private String username;
    private String profileImageUrl;
    private Integer subscribeState;
    private Integer equalUserState;
}
