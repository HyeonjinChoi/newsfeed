package com.sparta.newspeed.comment.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CommentRequestDtoTest {

    private final Validator validator;

    public CommentRequestDtoTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("댓글 요청 DTO 유효성 검사 성공 테스트")
    void testCommentRequestDtoValidationSuccess() {
        // given
        CommentRequestDto requestDto = CommentRequestDto.builder()
                .content("Valid Comment")
                .build();

        // when
        Set<ConstraintViolation<CommentRequestDto>> violations = validator.validate(requestDto);

        // then
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("댓글 요청 DTO 유효성 검사 실패 테스트")
    void testCommentRequestDtoValidationFailure() {
        // given
        CommentRequestDto requestDto = new CommentRequestDto();
        // when
        Set<ConstraintViolation<CommentRequestDto>> violations = validator.validate(requestDto);

        // then
        assertEquals(1, violations.size());
        ConstraintViolation<CommentRequestDto> violation = violations.iterator().next();
        assertEquals("빈칸에 댓글을 작성하세요.", violation.getMessage());
    }
}