package com.example.odop.dto.Response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemoResponse {
    private Long memoId;
    private String imgUrl;
    private String nickName;
    private Integer page;
    private String memo;
}
