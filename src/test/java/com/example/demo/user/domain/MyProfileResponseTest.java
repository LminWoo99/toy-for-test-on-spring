package com.example.demo.user.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MyProfileResponseTest {
    @Test
    public void User로_응답을_생성할_수있다() throws Exception{
        //given
        User user = User.builder()
                .email("mw310@naver.com")
                .nickname("manu")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa")
                .build();

        //when
        MyProfileResponse myProfileResponse = MyProfileResponse.from(user);

        //then
        assertThat(myProfileResponse.getEmail()).isEqualTo("mw310@naver.com");
        assertThat(myProfileResponse.getAddress()).isEqualTo("seoul");
        assertThat(myProfileResponse.getNickname()).isEqualTo("manu");
        assertThat(myProfileResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(myProfileResponse.getLastLoginAt()).isEqualTo(100L);
    }

}