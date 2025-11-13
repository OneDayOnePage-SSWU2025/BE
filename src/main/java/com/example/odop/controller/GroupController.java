package com.example.odop.controller;

import com.example.odop.dto.Request.GroupRequest;
import com.example.odop.dto.Request.SignUpRequest;
import com.example.odop.dto.Response.ApiListResponse;
import com.example.odop.dto.Response.ApiResponse;
import com.example.odop.service.GroupService;
import com.example.odop.service.JoinService;
import com.example.odop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;
    private final JoinService joinService;
    @Operation(summary = "그룹 생성", description = "그룹 정보/첫 책 정보 입력해서 생성. 책 정보들은 알라딘 api 호출을 선행해 얻어와야함.")
    @PostMapping
    public ResponseEntity<ApiResponse> makeGroup(@AuthenticationPrincipal(expression = "id") String id, @ModelAttribute GroupRequest req) {
        groupService.makeGroup(req, id);
        return ResponseEntity.ok(new ApiResponse(true, "그룹 생성 성공", null));
    }

    @Operation(summary = "그룹에 참가", description = "그룹명+참가코드로 찾아와서 가입할것")
    @PostMapping("/join")
    public ResponseEntity<ApiResponse> makeGroup(@AuthenticationPrincipal(expression = "id") String id, @RequestParam String groupName, @RequestParam Integer code) {
        joinService.makeJoin(id, groupName, code);
        return ResponseEntity.ok(new ApiResponse(true, "그룹 참여 성공", null));
    }

    @Operation(summary = "그룹을 만든 사람인지 확인하는거", description = "그룹 아이디 넘겨야함")
    @GetMapping("/isOwner")
    public ResponseEntity<ApiResponse> makeGroup(@AuthenticationPrincipal(expression = "id") String id, @RequestParam Long groupId) {
        return ResponseEntity.ok(new ApiResponse(true, "그룹 참여 성공", groupService.isOwner(id, groupId)));
    }

    @Operation(summary = "그룹들 조회 (펫 나오는거)", description = "상위 몇퍼인지랑 총 몇개 읽었는지(이걸로 펫 성장 상태 어케 보여줄 지 정하기) 돌려주는거")
    @GetMapping
    public ResponseEntity<?> getGroups(@AuthenticationPrincipal(expression = "id") String id) {
        return ResponseEntity.ok(joinService.getGroups(id));
    }
    @Operation(summary = "그룹들 조회 (책 표지 나오는거)", description = "책 표지랑 제목 등등 돌려주는거")
    @GetMapping("/detail")
    public ResponseEntity<?> getDetailGroups(@AuthenticationPrincipal(expression = "id") String id) {
        return ResponseEntity.ok(joinService.getDetailedGroups(id));
    }
}



