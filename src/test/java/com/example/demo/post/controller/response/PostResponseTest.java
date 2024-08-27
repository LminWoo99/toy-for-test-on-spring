package com.example.demo.post.controller.response;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PostResponseTest {

    @Test
    void Post으로_응답을_생설할_수_있다() throws Exception{
        //given
        Post post = Post.builder()
                .content("helloword")
                .writer(User.builder()
                        .email("mw310@naver.com")
                        .nickname("manu")
                        .address("seoul")
                        .status(UserStatus.ACTIVE)
                        .certificationCode("aaaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa")
                        .build())
                .build();
        //when
        PostResponse postResponse = PostResponse.from(post);

        //then
        Assertions.assertThat(postResponse.getContent()).isEqualTo("helloword");
        Assertions.assertThat(postResponse.getWriter().getEmail()).isEqualTo("mw310@naver.com");

    }

}