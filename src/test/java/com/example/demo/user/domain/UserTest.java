package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void User는_UserCreate_객체로_생성할_수_있다() throws Exception{
        //given
        UserCreate userCreate = UserCreate.builder()
                .email("mw310@naver.com")
                .nickname("manu")
                .address("seoul")
                .build();
        //when
        User user = User.from(userCreate, new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"));
        //then
        assertThat(user.getId()).isNull();
        assertThat(user.getNickname()).isEqualTo("manu");
        assertThat(user.getAddress()).isEqualTo("seoul");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    }
    @Test
    public void User는_UserUpdate_객체로_데이터를_업데이트_할_수_있다() throws Exception{
        //given
        User user = User.builder()
                .id(1L)
                .email("mw310@naver.com")
                .nickname("manu")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa")
                .build();
        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("manu-update")
                .address("NewYork")
                .build();

        //when
        user = user.update(userUpdate);
        //then
        assertThat(user.getNickname()).isEqualTo("manu-update");
        assertThat(user.getAddress()).isEqualTo("NewYork");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa");

    }
    @Test
    public void User는_로그인을_할_수_있고_로그인시_마지막_로그인_시간이_변경된다() throws Exception{
        //given
        User user = User.builder()
                .id(1L)
                .email("mw310@naver.com")
                .nickname("manu")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa")
                .build();

        //when
        user = user.login(new TestClockHolder(1678530673958L));

        //then
        assertThat(user.getLastLoginAt()).isEqualTo(1678530673958L);
    } @Test
    public void User는_유효한_인증_코드로_계정을_활성화_할_수있다() throws Exception{
        //given
        User user = User.builder()
                .id(1L)
                .email("mw310@naver.com")
                .nickname("manu")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa")
                .build();
        //when
        user = user.certificate("aaaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa");
        //then
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    } @Test
    public void User는_잘못된_인증_코드로_계정을_활성화_하려하면_에러를_던진다() throws Exception{
        //given
        User user = User.builder()
                .id(1L)
                .email("mw310@naver.com")
                .nickname("manu")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa")
                .build();
        //when
                //then
        assertThatThrownBy(() ->
                user.certificate("aaaaaaaaa-aaaa-aaaa-aaaa-aaaaaaab")
        ).isInstanceOf(CertificationCodeNotMatchedException.class);

    }

}