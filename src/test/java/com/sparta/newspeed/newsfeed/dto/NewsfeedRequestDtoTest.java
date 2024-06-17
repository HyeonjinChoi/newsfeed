package com.sparta.newspeed.newsfeed.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NewsfeedRequestDtoTest {

    private final Validator validator;

    public NewsfeedRequestDtoTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("뉴스피드 요청 DTO 유효성 검사 성공 테스트")
    void testNewsfeedRequestDtoValidationSuccess() {
        // given
        NewsfeedRequestDto requestDto = NewsfeedRequestDto.builder()
                .title("Valid Title")
                .content("Valid Content")
                .ottName("Netflix")
                .remainMember(2)
                .build();

        // when
        Set<ConstraintViolation<NewsfeedRequestDto>> violations = validator.validate(requestDto);

        // then
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("뉴스피드 요청 DTO 유효성 검사 실패 테스트")
    void testNewsfeedRequestDtoValidationFailure() {
        // given
        NewsfeedRequestDto requestDto = NewsfeedRequestDto.builder()
                .build();

        // when
        Set<ConstraintViolation<NewsfeedRequestDto>> violations = validator.validate(requestDto);

        // then
        assertEquals(4, violations.size());
    }
}