package com.sparta.newspeed.newsfeed.controller;

import com.sparta.newspeed.common.exception.CustomException;
import com.sparta.newspeed.common.exception.ErrorCode;
import com.sparta.newspeed.config.WebSecurityConfig;
import com.sparta.newspeed.newsfeed.dto.NewsfeedRequestDto;
import com.sparta.newspeed.newsfeed.dto.NewsfeedResponseDto;
import com.sparta.newspeed.newsfeed.service.NewsfeedService;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
        controllers = {NewsfeedController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
class NewsfeedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsfeedService newsfeedService;

    @Test
    @WithMockUser
    @DisplayName("뉴스피드 생성 - 성공")
    void createNewsfeed() throws Exception {
        NewsfeedRequestDto requestDto = NewsfeedRequestDto.builder()
                .title("Test Title")
                .content("Test Content")
                .ottName("Netflix")
                .remainMember(2)
                .build();

        NewsfeedResponseDto responseDto = NewsfeedResponseDto.builder()
                .newsFeedSeq(1L)
                .title("Test Title")
                .content("Test Content")
                .remainMember(2)
                .userName("TestUser")
                .ottName("Netflix")
                .build();

        when(newsfeedService.createNewsFeed(any(NewsfeedRequestDto.class), any(User.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/newsfeeds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test Title\", \"content\":\"Test Content\", \"ottName\":\"Netflix\", \"remainMember\":2}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.newsFeedSeq").value(1L))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.content").value("Test Content"))
                .andExpect(jsonPath("$.remainMember").value(2))
                .andExpect(jsonPath("$.userName").value("TestUser"))
                .andExpect(jsonPath("$.ottName").value("Netflix"));
    }

    @Test
    @WithMockUser
    @DisplayName("뉴스피드 전체 조회 - 성공")
    void getAllNewsfeeds() throws Exception {
        NewsfeedResponseDto responseDto1 = NewsfeedResponseDto.builder()
                .newsFeedSeq(1L)
                .title("Test Title 1")
                .content("Test Content 1")
                .remainMember(2)
                .userName("TestUser1")
                .ottName("Netflix")
                .build();

        NewsfeedResponseDto responseDto2 = NewsfeedResponseDto.builder()
                .newsFeedSeq(2L)
                .title("Test Title 2")
                .content("Test Content 2")
                .remainMember(3)
                .userName("TestUser2")
                .ottName("Hulu")
                .build();

        List<NewsfeedResponseDto> responseList = Arrays.asList(responseDto1, responseDto2);

        when(newsfeedService.getNewsfeeds(anyInt(), anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(responseList);

        mockMvc.perform(get("/api/newsfeeds"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].newsFeedSeq").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test Title 1"))
                .andExpect(jsonPath("$[1].newsFeedSeq").value(2L))
                .andExpect(jsonPath("$[1].title").value("Test Title 2"));
    }

    @Test
    @WithMockUser
    @DisplayName("뉴스피드 단일 조회 - 성공")
    void getNewsfeedById() throws Exception {
        NewsfeedResponseDto responseDto = NewsfeedResponseDto.builder()
                .newsFeedSeq(1L)
                .title("Test Title")
                .content("Test Content")
                .remainMember(2)
                .userName("TestUser")
                .ottName("Netflix")
                .build();

        when(newsfeedService.getNewsfeed(anyLong())).thenReturn(responseDto);

        mockMvc.perform(get("/api/newsfeeds/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.newsFeedSeq").value(1L))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.content").value("Test Content"))
                .andExpect(jsonPath("$.remainMember").value(2))
                .andExpect(jsonPath("$.userName").value("TestUser"))
                .andExpect(jsonPath("$.ottName").value("Netflix"));
    }

    @Test
    @WithMockUser
    @DisplayName("뉴스피드 수정 - 성공")
    void updateNewsfeed() throws Exception {
        NewsfeedRequestDto requestDto = NewsfeedRequestDto.builder()
                .title("Updated Title")
                .content("Updated Content")
                .ottName("Netflix")
                .remainMember(3)
                .build();

        NewsfeedResponseDto responseDto = NewsfeedResponseDto.builder()
                .newsFeedSeq(1L)
                .title("Updated Title")
                .content("Updated Content")
                .remainMember(3)
                .userName("TestUser")
                .ottName("Netflix")
                .build();

        when(newsfeedService.updateNewsFeed(anyLong(), any(NewsfeedRequestDto.class), any(User.class)))
                .thenReturn(responseDto);

        mockMvc.perform(put("/api/newsfeeds/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Title\", \"content\":\"Updated Content\", \"ottName\":\"Netflix\", \"remainMember\":3}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.newsFeedSeq").value(1L))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.content").value("Updated Content"))
                .andExpect(jsonPath("$.remainMember").value(3))
                .andExpect(jsonPath("$.userName").value("TestUser"))
                .andExpect(jsonPath("$.ottName").value("Netflix"));
    }

    @Test
    @WithMockUser
    @DisplayName("뉴스피드 삭제 - 성공")
    void deleteNewsfeed() throws Exception {
        Mockito.doNothing().when(newsfeedService).deleteNewsFeed(anyLong(), any(User.class));

        mockMvc.perform(delete("/api/newsfeeds/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    @DisplayName("뉴스피드 생성 - 제목 누락 예외")
    void createNewsfeedMissingTitle() throws Exception {
        mockMvc.perform(post("/api/newsfeeds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"Test Content\", \"ottName\":\"Netflix\", \"remainMember\":2}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("뉴스피드 조회 - 뉴스피드 없음 예외")
    void getNewsfeedNotFound() throws Exception {
        when(newsfeedService.getNewsfeed(anyLong())).thenThrow(new CustomException(ErrorCode.NEWSFEED_NOT_FOUND));

        mockMvc.perform(get("/api/newsfeeds/1"))
                .andExpect(status().isNotFound());
    }
}