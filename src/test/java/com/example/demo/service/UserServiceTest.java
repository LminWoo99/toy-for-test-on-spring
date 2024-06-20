package com.example.demo.service;

import com.example.demo.exception.CertificationCodeNotMatchedException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.UserCreateDto;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.context.jdbc.Sql.*;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")

@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다() throws Exception{
        //given
        String email = "mw310@naver.com";

        //when
        UserEntity result = userService.getByEmail(email);
        //then
        assertThat(result.getNickname()).isEqualTo("manu");
    }
    @Test
    void getByEmail은_PENDING_상태인_유저를_찾아올_수_없다() throws Exception{
        //given
        String email = "mw410@naver.com";

        //when
        //then
        assertThatThrownBy(() -> {
            UserEntity result = userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }
    @Test
    void getById은_ACTIVE_상태인_유저를_찾아올_수_있다() throws Exception{
        //given
        //when
        UserEntity result = userService.getById(1);
        //then
        assertThat(result.getNickname()).isEqualTo("manu");
    }
    @Test
    void getById은_PENDING_상태인_유저를_찾아올_수_없다() throws Exception{
        //given
        //when
        //then
        assertThatThrownBy(() -> {
            UserEntity result = userService.getById(2);
        }).isInstanceOf(ResourceNotFoundException.class);
    }
    @Test
    void userCreateDto_를_이용하여_유저를_생성할_수_있다() throws Exception{
        //given
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .email("mw310@kakao.com")
                .address("Gyeongi")
                .nickname("manu2")
                .build();
        BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
        //when
        UserEntity result = userService.create(userCreateDto);
        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
//        assertThat(result.getCertificationCode()).isEqualTo("ㅠㅠ");// FIXME
    }
    @Test
    void userUpdateDto_를_이용하여_유저를_수정할_수_있다() throws Exception{
        //given
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .address("Incheon")
                .nickname("manu3")
                .build();
        //when
        userService.update(1, userUpdateDto);
        //then
        UserEntity result = userService.getById(1);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getAddress()).isEqualTo("Incheon");
        assertThat(result.getNickname()).isEqualTo("manu3");
//        assertThat(result.getCertificationCode()).isEqualTo("ㅠㅠ");// FIXME
    }
    @Test
    void user를_로그인_시키면_마지막_로그인_시간이_변경된다() throws Exception{
        //when
        userService.login(1);

        //then
        UserEntity result = userService.getById(1);
        assertThat(result.getLastLoginAt()).isGreaterThan(0L);
//        assertThat(result.getCertificationCode()).isEqualTo("ㅠㅠ"); // FIXME
    }
    @Test
    void PENDING_상태의_사용자는_인증_코드를_ACTIVE_시킬_수_있다() throws Exception{
        //when
        String uuid = "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab";
        userService.verifyEmail(2, uuid);

        //then
        UserEntity result = userService.getById(2);
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }
    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() throws Exception{
        //given
        // when
        String uuid = "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac";
        //then
        assertThatThrownBy(() -> {
            userService.verifyEmail(2, uuid);
        }).isInstanceOf(CertificationCodeNotMatchedException.class);

    }


}