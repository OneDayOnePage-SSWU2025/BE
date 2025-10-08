package com.example.odop.dto.Request;// UpdateNicknameRequest.java
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateNicknameRequest {
    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickName;
}