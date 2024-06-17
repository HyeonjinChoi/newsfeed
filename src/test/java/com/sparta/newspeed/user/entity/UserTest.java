package com.sparta.newspeed.user.entity;

import com.sparta.newspeed.user.dto.UserInfoUpdateDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;

    @Test
    @DisplayName("사용자 생성 테스트")
    void testCreateUser() {
        // given
        String userId = "userUser1234";
        String userPassword = "user1234!@#$";
        String userName = "User";
        UserRoleEnum role = UserRoleEnum.USER;

        // when
        User user = User.builder()
                .userId(userId)
                .userPassword(userPassword)
                .userName(userName)
                .role(role)
                .build();

        // then
        assertNotNull(user);
        assertEquals(userId, user.getUserId());
        assertEquals(userPassword, user.getUserPassword());
        assertEquals(role, user.getRole());
    }

    @Test
    @DisplayName("사용자 정보 수정 테스트")
    void testUpdateUserInfo() {
        // given
        UserInfoUpdateDto requestDto = UserInfoUpdateDto.builder()
                .name("New User")
                .build();

        // when
        user.updateUserInfo(requestDto);

        // Then
        assertEquals("New User", user.getUserName());
    }

    @Test
    @DisplayName("사용자 소셜로그인 정보 수정 테스트")
    void testUpdateOAuth2Info() {
        // given
        String updatedUserName = "Updated User";
        String updatedProfileImageUrl = "http://example.com/updated_profile.jpg";

        // when
        user.updateOAuth2Info(updatedUserName, updatedProfileImageUrl);

        // then
        assertEquals(updatedUserName, user.getUserName());
        assertEquals(updatedProfileImageUrl, user.getProfileImageUrl());
    }

    @Test
    @DisplayName("사용자 비밀번호 수정 테스트")
    void testUpdatePassword() {
        // Given
        String newPassword = "newPassword123!@#";

        // When
        user.updatePassword(newPassword);

        // Then
        assertEquals(newPassword, user.getUserPassword());
    }

    @Test
    @DisplayName("사용자 권한 수정 테스트")
    void testUpdateRole() {
        // Given
        UserRoleEnum newRole = UserRoleEnum.ADMIN;

        // When
        user.updateRole(newRole);

        // Then
        assertEquals(newRole, user.getRole());
    }

    @Test
    @DisplayName("리프레시 토큰 수정 테스트")
    void testSetRefreshToken() {
        // Given
        String token = "newRefreshToken";

        // When
        user.setRefreshToken(token);

        // Then
        assertEquals(token, user.getRefreshToken());
    }
}