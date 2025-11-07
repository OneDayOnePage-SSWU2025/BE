package com.example.odop.service;

import com.example.odop.dto.Request.LoginRequest;
import com.example.odop.dto.Request.SignUpRequest;
import com.example.odop.dto.Request.UserInfo;
import com.example.odop.dto.Response.MyPageResponse;
import com.example.odop.entity.Users;
import com.example.odop.jwt.JwtUtil;
import com.example.odop.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final GCPService s3Service;
    private final ReportService reportService;

    @Transactional
    public void SignUp(SignUpRequest req) throws IOException {
        if (req == null) {
            throw new IllegalArgumentException("요청이 올바르지 않습니다.");
        }
        if (req.getId() == null || req.getId().isBlank()) {
            throw new IllegalArgumentException("아이디는 필수입니다.");
        }
        if (req.getPassword() == null || req.getPassword().isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }
        if(userRepository.existsById(req.getId())){
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        Users entity = new Users();
        entity.setId(req.getId());
        entity.setPassword(encoder.encode(req.getPassword()));
        entity.setNickName(req.getNickName());
        MultipartFile image = req.getImg();

        if (image != null && !image.isEmpty()) {
            System.out.println(req.getImg().getOriginalFilename());
            String url = s3Service.upload(image, "profile");
            entity.setImgUrl(url);
        }
        userRepository.save(entity);
        reportService.makeReport(entity);
    }

    @Transactional
    public String login(LoginRequest loginRequest) {
        Users user = userRepository.findById(loginRequest.getUsername()).orElseThrow(()->new UsernameNotFoundException("존재하지 않는 유저입니다."));
        if(!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }
        UserInfo userInfo = UserInfo.toDto(user);
        return jwtUtil.createAccessToken(userInfo);
    }

    @Transactional
    public void updateNickname(String userId, String newNickname) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));
        user.setNickName(newNickname);
    }

    @Transactional
    public String updateProfileImage(String userId, MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("이미지 파일이 비어 있습니다.");
        }
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));

        String url = s3Service.upload(image, "profile");
        user.setImgUrl(url);
        return url;
    }

    @Transactional
    public MyPageResponse getMyInfo(String id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));

        return MyPageResponse.builder()
                .userId(user.getUserId())
                .id(user.getId())
                .nickName(user.getNickName())
                .imgUrl(user.getImgUrl())
                .createdDate(user.getCreatedDate())   // BaseTimeEntity 필드 가정
                .modifiedDate(user.getModifiedDate())
                .build();
    }


}
