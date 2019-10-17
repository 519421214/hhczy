package com.king.hhczy.entity.model;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class PostList {
    private int id;
    private String avatar;
    private LocalDateTime dateTime;
    private String title;
    private String imgSrc;
    private String content;
    private int collection;
    private int reading;
}
