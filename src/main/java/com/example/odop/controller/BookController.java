package com.example.odop.controller;

import com.example.odop.dto.Request.BookRequest;
import com.example.odop.dto.Request.GroupRequest;
import com.example.odop.dto.Request.MemoRequest;
import com.example.odop.dto.Response.ApiListResponse;
import com.example.odop.dto.Response.ApiResponse;
import com.example.odop.dto.Response.MemoResponse;
import com.example.odop.service.BookService;
import com.example.odop.service.GroupService;
import com.example.odop.service.MemoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final MemoService memoService;

    @Operation(summary = "책추가(책변경)", description = "추가한 책으로 변경되는거임")
    @PostMapping
    public ResponseEntity<ApiResponse> addBook(@AuthenticationPrincipal(expression = "id") String id, @RequestParam Long groupId, @Valid @RequestBody BookRequest req) throws IllegalAccessException {
        bookService.addBook(id, req, groupId);
        return ResponseEntity.ok(new ApiResponse(true, "그룹 생성 성공", null));
    }

    @Operation(summary = "책 총 페이지 수", description = "책 아이디 주면 그 책 총 페이지 수 리턴")
    @GetMapping("/total_page")
    public ResponseEntity<ApiResponse> getTotalPage(@RequestParam Long bookId){
        return ResponseEntity.ok(new ApiResponse(true, "책 페이지 조회 성공", bookService.getBookTotalPage(bookId)));
    }

    @Operation(summary = "메모 작성", description = "책 아이디, 내용, 페이지 적으면됨")
    @PostMapping("/memo")
    public ResponseEntity<ApiResponse> makeMemo(@AuthenticationPrincipal(expression = "id") String id, @Valid @RequestBody MemoRequest req){
        memoService.makeMemo(id, req);
        return ResponseEntity.ok(new ApiResponse(true, "책 페이지 조회 성공",null));
    }
    //특정 책의 특정 페이지에 달린 메모 목록
    @Operation(summary = "페이지별 메모 목록", description = "책 ID와 페이지 번호로 메모 목록을 조회합니다.")
    @GetMapping("/memo_by_page")
    public ResponseEntity<ApiListResponse<MemoResponse>> getMemosByPage(
            @RequestParam Long bookId,
            @RequestParam @Min(1) Integer page
    ) {
        var resp = memoService.getMemosByPage(bookId, page);
        return ResponseEntity.ok(resp);
    }

    // 내가 쓴 메모 목록(책 기준)
    @Operation(summary = "내 메모 목록(책 기준)", description = "현재 로그인 사용자가 해당 책에 작성한 메모 목록을 조회합니다.")
    @GetMapping("/my_memo")
    public ResponseEntity<ApiListResponse<MemoResponse>> getMemosByMe(
            @AuthenticationPrincipal(expression = "id") String id,
            @RequestParam Long bookId
    ) {
        var resp = memoService.getMemosByMe(id, bookId);
        return ResponseEntity.ok(resp);
    }

    // 메모 단건 조회
    @Operation(summary = "메모 단건 조회", description = "메모 ID로 단건 메모를 조회합니다.")
    @GetMapping("/{memoId}")
    public ResponseEntity<ApiResponse> getMemo(@PathVariable Long memoId) {
        MemoResponse data = memoService.getMemo(memoId);
        return ResponseEntity.ok(new ApiResponse(true, "메모 조회 성공", data));
    }

    // 해당 메모가 내 것인지 여부
    @Operation(summary = "메모 소유 여부", description = "해당 메모가 현재 로그인 사용자의 것인지 여부를 반환합니다.")
    @GetMapping("/memo/is-mine")
    public ResponseEntity<ApiResponse> isMine(
            @AuthenticationPrincipal(expression = "id") String id,
            @RequestParam Long memoId
    ) {
        Boolean mine = memoService.isMine(id, memoId);
        return ResponseEntity.ok(new ApiResponse(true, "메모 소유 여부 조회 성공", mine));
    }

    // 메모 삭제 (본인 소유만 가능)
    @Operation(summary = "메모 삭제", description = "메모 ID로 메모를 삭제합니다. 본인 소유만 삭제할 수 있습니다.")
    @DeleteMapping("/{memoId}")
    public ResponseEntity<ApiResponse> deleteMemo(
            @AuthenticationPrincipal(expression = "id") String id,
            @PathVariable Long memoId
    ) {
        memoService.deleteMemo(id, memoId);
        return ResponseEntity.ok(new ApiResponse(true, "메모가 삭제되었습니다.", null));
    }


}
