package com.example.odop.dto.Response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupEditResponse {
    private Long groupId;
    private String groupName;
    private Integer code;
    private String theme;
    private List<String> bookNames;
}
