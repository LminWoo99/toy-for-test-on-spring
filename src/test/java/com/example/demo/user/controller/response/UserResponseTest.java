package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserResponseTest {

    @Test
    public void USER으로_응답을_생설할_수_있다() throws Exception{
        //given
        User user = User.builder()
                .id(1L)
                .email("mw310@naver.com")
                .nickname("manu")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa")
                .build();
        //when
        UserResponse userResponse = UserResponse.from(user);
        //then
        assertThat(userResponse.getEmail()).isEqualTo("mw310@naver.com");
        assertThat(userResponse.getNickname()).isEqualTo("manu");
        assertThat(userResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(userResponse.getLastLoginAt()).isEqualTo(100L);

    }

}