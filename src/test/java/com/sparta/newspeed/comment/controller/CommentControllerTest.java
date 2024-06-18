package com.sparta.newspeed.comment.controller;

import com.sparta.newspeed.comment.dto.CommentRequestDto;
import com.sparta.newspeed.comment.dto.CommentResponseDto;
import com.sparta.newspeed.comment.service.CommentService;
import com.sparta.newspeed.common.exception.CustomException;
import com.sparta.newspeed.common.exception.ErrorCode;
import com.sparta.newspeed.config.WebSecurityConfig;
import com.sparta.newspeed.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {CommentController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    @WithMockUser
    @DisplayName("댓글 생성 - 성공")
    void createComment() throws Exception {
        CommentRequestDto requestDto = new CommentRequestDto("Test Comment");
        CommentResponseDto responseDto = CommentResponseDto.builder()
                .userName("TestUser")
                .content("Test Comment")
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        when(commentService.createComment(anyLong(), any(CommentRequestDto.class), any(User.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/newsfeeds/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"Test Comment\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userName").value("TestUser"))
                .andExpect(jsonPath("$.content").value("Test Comment"));
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 조회 - 성공")
    void getComments() throws Exception {
        CommentResponseDto responseDto1 = CommentResponseDto.builder()
                .userName("TestUser1")
                .content("Test Comment 1")
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        CommentResponseDto responseDto2 = CommentResponseDto.builder()
                .userName("TestUser2")
                .content("Test Comment 2")
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        List<CommentResponseDto> responseList = Arrays.asList(responseDto1, responseDto2);

        when(commentService.findAll(anyLong())).thenReturn(responseList);

        mockMvc.perform(get("/api/newsfeeds/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userName").value("TestUser1"))
                .andExpect(jsonPath("$[0].content").value("Test Comment 1"))
                .andExpect(jsonPath("$[1].userName").value("TestUser2"))
                .andExpect(jsonPath("$[1].content").value("Test Comment 2"));
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 수정 - 성공")
    void updateComment() throws Exception {
        CommentRequestDto requestDto = new CommentRequestDto("Updated Comment");
        CommentResponseDto responseDto = CommentResponseDto.builder()
                .userName("TestUser")
                .content("Updated Comment")
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        when(commentService.updateComment(anyLong(), anyLong(), any(CommentRequestDto.class), any(User.class)))
                .thenReturn(responseDto);

        mockMvc.perform(put("/api/newsfeeds/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"Updated Comment\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("TestUser"))
                .andExpect(jsonPath("$.content").value("Updated Comment"));
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 삭제 - 성공")
    void deleteComment() throws Exception {
        Mockito.doNothing().when(commentService).deleteComment(anyLong(), anyLong(), any(User.class));

        mockMvc.perform(delete("/api/newsfeeds/1/comments/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 생성 - 내용 누락 예외")
    void createCommentMissingContent() throws Exception {
        mockMvc.perform(post("/api/newsfeeds/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 조회 - 댓글 없음 예외")
    void getCommentsNotFound() throws Exception {
        when(commentService.findAll(anyLong())).thenThrow(new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        mockMvc.perform(get("/api/newsfeeds/1/comments"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 수정 - 사용자 불일치 예외")
    void updateCommentNotUser() throws Exception {
        when(commentService.updateComment(anyLong(), anyLong(), any(CommentRequestDto.class), any(User.class)))
                .thenThrow(new CustomException(ErrorCode.COMMENT_NOT_USER));

        mockMvc.perform(put("/api/newsfeeds/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"Updated Comment\"}"))
                .andExpect(status().isForbidden());
    }
}