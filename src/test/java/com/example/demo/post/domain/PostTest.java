package com.example.demo.post.domain;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class PostTest {

    @Test
    void Post는_PostCreate_객체로_생성할_수_있다() throws Exception{
        //given
        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("helloword")
                .build();
        User writer = User.builder()
                .email("mw310@naver.com")
                .nickname("mw310")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode(UUID.randomUUID().toString())
                .build();
        //when
        Post post = Post.from(writer, postCreate,new TestClockHolder(1678530673958L));
        //then
        Assertions.assertThat(post.getContent()).isEqualTo("helloword");
        Assertions.assertThat(post.getWriter().getEmail()).isEqualTo("mw310@naver.com");
        assertThat(post.getCreatedAt()).isEqualTo(1678530673958L);

    }
    @Test
    void PostUpdate로_게시물을_수정할_수_있다() throws Exception{
        //given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("foobar")
                .build();
        User writer = User.builder()
                .email("mw310@naver.com")
                .nickname("mw310")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode(UUID.randomUUID().toString())
                .build();
        Post post=Post.builder()
                .id(1L)
                .content("helloworld")
                .createdAt(1678530673958L)
                .modifiedAt(0L)
                .writer(writer)
                .build();
        //when
        post = post.update(postUpdate,new TestClockHolder(1678530673958L));
        //then
        Assertions.assertThat(post.getContent()).isEqualTo("foobar");
        assertThat(post.getModifiedAt()).isEqualTo(1678530673958L);

    }

 }