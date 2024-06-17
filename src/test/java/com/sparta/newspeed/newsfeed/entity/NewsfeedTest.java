package com.sparta.newspeed.newsfeed.entity;

import com.sparta.newspeed.comment.dto.CommentRequestDto;
import com.sparta.newspeed.newsfeed.dto.NewsfeedRequestDto;
import com.sparta.newspeed.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NewsfeedTest {

    private Newsfeed newsfeed;

    @Test
    @DisplayName("뉴스피드 생성 테스트")
    void testCreateNewsfeed() {
        // given
        String title = "Title";
        String content = "Content";
        int remainMember = 3;
        Long likes = 0L;
        User user = new User();
        Ott ott = Ott.builder()
                .ottName("Netflix")
                .build();

        // when
        newsfeed = Newsfeed.builder()
                .title(title)
                .content(content)
                .remainMember(remainMember)
                .like(likes)
                .user(user)
                .ott(ott)
                .build();

        // then
        assertNotNull(newsfeed);
        assertEquals(title, newsfeed.getTitle());
        assertEquals(content, newsfeed.getContent());
        assertEquals(remainMember, newsfeed.getRemainMember());
        assertEquals(likes, newsfeed.getLike());
        assertEquals(user, newsfeed.getUser());
        assertEquals("Netflix", newsfeed.getOtt().getOttName());
    }

    @Test
    @DisplayName("뉴스피드 수정 테스트")
    void testUpdateNewsfeed() {
        // given
        NewsfeedRequestDto requestDto = NewsfeedRequestDto.builder()
                .title("Updated Title")
                .content("Updated Content")
                .build();
        Ott ott = Ott.builder()
                .ottName("Watcha")
                .build();

        // when
        newsfeed.updateNewsfeed(requestDto, ott);

        // then
        assertEquals("Updated Title", newsfeed.getTitle());
        assertEquals("Updated Content", newsfeed.getContent());
        assertEquals("Watcha", newsfeed.getOtt().getOttName());
    }

    @Test
    @DisplayName("뉴스피드 좋아요 증가 테스트")
    void testIncreaseLike() {
        // given
        Long initialLike = newsfeed.getLike();

        // when
        newsfeed.increaseLike();

        // then
        assertEquals(initialLike + 1, newsfeed.getLike());
    }

    @Test
    @DisplayName("뉴스피드 좋아요 감소 테스트")
    void testDecreaseLike() {
        // given
        Long initialLike = newsfeed.getLike();

        // when
        newsfeed.decreaseLike();

        // then
        assertEquals(initialLike - 1, newsfeed.getLike());
    }
}