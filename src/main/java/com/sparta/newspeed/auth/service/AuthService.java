package com.sparta.newspeed.auth.service;

import com.sparta.newspeed.auth.dto.LoginRequestDto;
import com.sparta.newspeed.auth.dto.SignUpRequestDto;
import com.sparta.newspeed.auth.dto.SignupResponseDto;
import com.sparta.newspeed.auth.dto.TokenDto;
import com.sparta.newspeed.common.exception.CustomException;
import com.sparta.newspeed.common.exception.ErrorCode;
import com.sparta.newspeed.security.util.JwtUtil;
import com.sparta.newspeed.user.entity.User;
import com.sparta.newspeed.user.entity.UserRoleEnum;
import com.sparta.newspeed.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public SignupResponseDto signup(SignUpRequestDto request) {
        String userId = request.getUserId();
        String userName = request.getUserName();
        String password = passwordEncoder.encode(request.getPassword());
        String email = request.getEmail();

        // 회원 아이디 중복 확인
        Optional<User> checkUsername = userRepository.findByUserId(userId);
        if (checkUsername.isPresent()) {
            throw new CustomException(ErrorCode.USER_NOT_UNIQUE);
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;

        // 사용자 등록
        User user = User.builder()
                .userId(userId)
                .userName(userName)
                .userPassword(password)
                .userEmail(email)
                .role(role)
                .build();

        userRepository.save(user);

        return new SignupResponseDto(user);
    }

    @Transactional
    public TokenDto login(LoginRequestDto requestDto, HttpServletResponse response) {
        String userId = requestDto.getUserId();
        String password = requestDto.getPassword();

        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        if((!passwordEncoder.matches(password, user.getUserPassword()))) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        TokenDto token = jwtUtil.createToken(userId, UserRoleEnum.USER);
        user.setRefreshToken(token.getRefreshToken());
        userRepository.save(user);
        return jwtUtil.createToken(userId, UserRoleEnum.USER);
    }

    public String reAuth(String refreshtoken) {
        String subToken = jwtUtil.substringToken(refreshtoken);
        User user = userRepository.findByRefreshToken(refreshtoken).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );
        if(!jwtUtil.validateRefreshToken(subToken)) {
            throw new IllegalArgumentException("토큰이 만료되었습니다.");
        }
        if(!jwtUtil.substringToken(refreshtoken).equals(user.getRefreshToken())) {
            throw new IllegalArgumentException("토큰이 일치하지 않습니다.");
        }
        String userId = jwtUtil.getUserInfoFromToken(subToken).getSubject();
        return jwtUtil.createToken(userId, UserRoleEnum.USER).getAccessToken();
    }
}
