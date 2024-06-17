package com.sparta.newspeed.user.dto;

import com.sparta.newspeed.comment.dto.CommentRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserInfoUpdateDtoTest {

    private final Validator validator;

    public UserInfoUpdateDtoTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("사용자 정보 수정 DTO 유효성 검사 성공 테스트")
    void testUserInfoUpdateDtoValidationSuccess() {
        // given
        UserInfoUpdateDto userInfoUpdateDto = UserInfoUpdateDto.builder()
                .name("Valid Name")
                .intro("Valid Intro")
                .build();

        // when
        Set<ConstraintViolation<UserInfoUpdateDto>> violations = validator.validate(userInfoUpdateDto);

        // then
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("사용자 정보 수정 DTO 유효성 검사 실패 테스트")
    void testUserInfoUpdateDtoValidationFailure() {
        // given
        UserInfoUpdateDto dto = new UserInfoUpdateDto();

        // when
        Set<ConstraintViolation<UserInfoUpdateDto>> violations = validator.validate(dto);

        // then
        assertFalse(violations.isEmpty());
    }
}