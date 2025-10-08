package com.example.odop.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupDetailResponse {
    private Long groupId;
    private String groupName;
    private Integer latestMemoPage;
    private BookResponse book;
}
