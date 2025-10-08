package com.example.odop.config;// GlobalExceptionHandler.java
import com.example.odop.dto.Response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 로그인 관련(아이디/비번 틀림, 사용자 없음) → 401 (동일 응답으로 사용자 열거 방지)
    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<ApiResponse> handleAuthFailure(RuntimeException e) {
        log.warn("[AUTH] {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse(false, "아이디 또는 비밀번호가 올바르지 않습니다.", null));
    }

    // 가입/요청값 오류 등 클라이언트 잘못 → 400
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleBadRequest(IllegalArgumentException e) {
        log.warn("[BAD_REQUEST] {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(false, e.getMessage(), null));
    }

    // DTO @Valid 검증 실패 → 400 (필드별 메시지 제공)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidation(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(fe -> errors.put(fe.getField(), fe.getDefaultMessage()));
        log.warn("[VALIDATION] {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(false, "요청 값이 올바르지 않습니다.", errors));
    }

    // JSON 파싱/형변환 오류 → 400
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleNotReadable(HttpMessageNotReadableException e) {
        log.warn("[NOT_READABLE] {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(false, "요청 본문을 읽을 수 없습니다.", null));
    }

    // 파일 업로드 제한 초과 → 413
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse> handleUploadLimit(MaxUploadSizeExceededException e) {
        log.warn("[UPLOAD_LIMIT] {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(new ApiResponse(false, "업로드 가능한 파일 용량을 초과했습니다.", null));
    }

    // 그 외 예상치 못한 오류 → 500 (내부 메시지는 노출하지 않음)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleEtc(Exception e) {
        log.error("[UNEXPECTED]", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "서버 오류가 발생했습니다.", null));
    }
}
