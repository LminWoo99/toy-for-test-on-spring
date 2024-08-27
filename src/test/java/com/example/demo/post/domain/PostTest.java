package com.example.demo.post.domain;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

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
        Post post = Post.from(writer, postCreate);
        //then
        Assertions.assertThat(post.getContent()).isEqualTo("helloword");
        Assertions.assertThat(post.getWriter().getEmail()).isEqualTo("mw310@naver.com");


    }

 }