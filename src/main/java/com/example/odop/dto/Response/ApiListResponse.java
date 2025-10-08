// ApiListResponse.java
package com.example.odop.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiListResponse<T> {
    private boolean success;
    private String message;
    private int totalCount;
    private List<T> data;
}
