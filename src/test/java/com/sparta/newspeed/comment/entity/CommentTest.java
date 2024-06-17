package com.sparta.newspeed.comment.entity;

import com.sparta.newspeed.comment.dto.CommentRequestDto;
import com.sparta.newspeed.newsfeed.entity.Newsfeed;
import com.sparta.newspeed.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CommentTest {

    Comment comment;

    @Test
    @DisplayName(" 댓글 생성 테스트")
    void testCreateComment() {
        // given
        User user = new User();
        Newsfeed newsfeed = new Newsfeed();
        String content = "Content";
        Long likes = 0L;

        // when
        comment = Comment.builder()
                .content(content)
                .like(likes)
                .user(user)
                .newsfeed(newsfeed)
                .build();

        // then
        assertNotNull(comment);
        assertEquals(content, comment.getContent());
        assertEquals(likes, comment.getLike());
        assertEquals(user, comment.getUser());
        assertEquals(newsfeed, comment.getNewsfeed());
    }

    @Test
    @DisplayName(" 댓글 수정 테스트")
    void testUpdateComment() {
        // given
        CommentRequestDto requestDto = CommentRequestDto.builder()
                .content("Updated Content")
                .build();

        // when
        comment.update(requestDto);

        // then
        assertEquals("Updated Content", comment.getContent());
    }

    @Test
    @DisplayName(" 댓글 좋아요 증가 테스트")
    void testIncreaseLike() {
        // given
        Long initialLike = comment.getLike();

        // when
        comment.increaseLike();

        // then
        assertEquals(initialLike + 1, comment.getLike());
    }

    @Test
    @DisplayName(" 댓글 좋아요 감소 테스트")
    void testDecreaseLike() {
        // given
        Long initialLike = comment.getLike();

        // when
        comment.decreaseLike();

        // then
        assertEquals(initialLike - 1, comment.getLike());
    }
}