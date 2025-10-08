package com.example.odop.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupResponse {
    private Long groupId;
    private Integer percentage; //상위 몇퍼인지 == 지금까지 읽은 책 수가 상위 몇 %인지
    private String groupName;
    private Integer petType;
    private Integer totalBook; // 이건 지금까지 읽은 책 수 -> 이거따라 ㅅ성장 정도 다르게 보여주면됨

}
