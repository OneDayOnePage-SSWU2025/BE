package com.example.odop.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupRequest {

    private String groupName;
    private Integer code;
    private String theme;

    private BookRequest book;
}