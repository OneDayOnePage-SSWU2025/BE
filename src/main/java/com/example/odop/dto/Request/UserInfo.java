package com.example.odop.dto.Request;

import com.example.odop.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private Long userId;
    private String password;
    private String id;

    public static UserInfo toDto(Users user) {
        return new UserInfo(
                user.getUserId(),
                user.getPassword(),
                user.getId()
        );
    }
}
