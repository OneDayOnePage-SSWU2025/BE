// MyPageResponse.java
package com.example.odop.dto.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyPageResponse {
    private Long userId;            // PK
    private String id;              // 로그인 아이디 (unique)
    private String nickName;
    private String imgUrl;          // 프로필 이미지 URL
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
