package com.example.odop.jwt;


import com.example.odop.dto.Request.UserInfo;
import com.example.odop.entity.Users;
import com.example.odop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Users user = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("해당 유저 없음"));

        UserInfo userInfo = UserInfo.toDto(user);
        return new CustomUserDetails(userInfo);
    }
}

