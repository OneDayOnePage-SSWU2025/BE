package com.example.odop.controller;
import com.example.odop.dto.Request.LoginRequest;
import com.example.odop.dto.Request.SignUpRequest;
import com.example.odop.dto.Request.UpdateNicknameRequest;
import com.example.odop.dto.Response.ApiResponse;
import com.example.odop.dto.Response.MyPageResponse;
import com.example.odop.dto.Response.ReportResponse;
import com.example.odop.service.ReportService;
import com.example.odop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ReportService reportService;

    @Operation(summary = "회원가입",
            description = "아이디/비밀번호/닉네임/이미지로 가입합니다. 이미지가 있기때문에 Postman form-data로 보내야합니다. 스웨거로 테스트 불가능")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signUp(@Valid @ModelAttribute SignUpRequest req) throws Exception {
        userService.SignUp(req);
        return ResponseEntity.ok(new ApiResponse(true, "회원가입 성공", null));
    }

    @Operation(summary = "로그인", description = "로그인 시 JWT 토큰을 반환합니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest req) {
        String token = userService.login(req);
        return ResponseEntity.ok(new ApiResponse(true, "로그인 성공", token));
    }

    @Operation(summary = "내 닉네임 수정", description = "현재 로그인한 사용자의 닉네임을 수정합니다.")
    @PatchMapping("/nickname")
    public ResponseEntity<ApiResponse> updateNickname(
            @AuthenticationPrincipal(expression = "id") String id,
            @Valid @RequestBody UpdateNicknameRequest req) {

        userService.updateNickname(id, req.getNickName());
        return ResponseEntity.ok(new ApiResponse(true, "닉네임이 수정되었습니다.", null));
    }

    @Operation(summary = "내 프로필 이미지 수정", description = "현재 로그인한 사용자의 프로필 이미지를 수정합니다.")
    @PatchMapping(value = "/image", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse> updateProfileImage(
            @AuthenticationPrincipal(expression = "id") String id,
            @RequestPart("image") MultipartFile image) throws IOException {

        String url = userService.updateProfileImage(id, image);
        return ResponseEntity.ok(new ApiResponse(true, "프로필 이미지가 수정되었습니다.", url));
    }

    @Operation(summary = "내 정보 조회", description = "마이페이지용 사용자 정보를 반환합니다.")
    @GetMapping("/my")
    public ResponseEntity<ApiResponse> getMyInfo(
            @AuthenticationPrincipal(expression = "id") String id) {

        MyPageResponse res = userService.getMyInfo(id);
        return ResponseEntity.ok(new ApiResponse(true, "내 정보 조회 성공", res));
    }
    @Operation(summary = "내 레포트 조회", description = "책 몇 권 메모 몇 개 썼는지 반환이요")
    @GetMapping("/report")
    public ResponseEntity<ApiResponse> getMyReport(
            @AuthenticationPrincipal(expression = "id") String id) {


        ReportResponse res = reportService.getMyReport(id);
        return ResponseEntity.ok(new ApiResponse(true, "내 독서리포트 조회 성공", res));
    }

}
