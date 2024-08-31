package com.example.demo.user.controller;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


class UserControllerTest {


    @Test
    void 사용자는_특정_유저의_정보를_개인정보는_소거된채_전달_받을_수_있다() throws Exception{
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("mw310@naver.com")
                .nickname("manu")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa")
                .lastLoginAt(100L)
                .build());

        ResponseEntity<UserResponse> result = testContainer.userController.getUserById(1);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody().getNickname()).isEqualTo("manu");
        assertThat(result.getBody().getEmail()).isEqualTo("mw310@naver.com");
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(100L);
    }
    @Test
    void 사용자는_존재하지_않는_유저의_아이디로_Api_호출할_경우_404_응답을_받는다() throws Exception{
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();
        //when
        //then
        assertThatThrownBy(() -> {
            testContainer.userController.getUserById(1);
        }).isInstanceOf(ResourceNotFoundException.class);

    }
    @Test
    void 사용자는_인증_코드로_계정을_활성화_시킬_수_있다() throws Exception{
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("mw310@naver.com")
                .nickname("manu")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaaaaaaaa-aaaa-aaaa-aaaa-aaaaaaab")
                .lastLoginAt(100L)
                .build());

        ResponseEntity<Void> result = testContainer.userController.verifyEmail(1, "aaaaaaaaa-aaaa-aaaa-aaaa-aaaaaaab");
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
        assertThat(testContainer.userRepository.getById(1).getStatus()).isEqualTo(UserStatus.ACTIVE);
    }
    @Test
    void 사용자는_인증_코드가_일치하지_않을_경우_권한_없음_에러를_내려준다() throws Exception{

        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("mw310@naver.com")
                .nickname("manu")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaaaaaaaa-aaaa-aaaa-aaaa-aaaaaaab")
                .lastLoginAt(100L)
                .build());
        assertThatThrownBy(() -> {
            testContainer.userController.verifyEmail(1, "aaaaaaaaa-aaaa-aaaa-aaaa-aaaaaab");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);

        //then

    }
    @Test
    void 시용자는_내_정보를_불러올_떄_개인정보인_주소도_갖고_올_수_있다() throws Exception{
        //given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 1679530673958L)
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("mw310@naver.com")
                .nickname("manu")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa")
                .lastLoginAt(100L)
                .build());

        ResponseEntity<MyProfileResponse> result = testContainer.userController.getMyInfo("mw310@naver.com");
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody().getEmail()).isEqualTo("mw310@naver.com");
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(1679530673958L);
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getAddress()).isEqualTo("Seoul");
    }
    @Test
    void 시용자는_내_정보를_수정할_수_있다() throws Exception{
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("mw310@naver.com")
                .nickname("manu")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa")
                .lastLoginAt(100L)
                .build());

        ResponseEntity<MyProfileResponse> result = testContainer.userController.updateMyInfo("mw310@naver.com", UserUpdate.builder()
                        .address("Pangyo")
                        .nickname("minu")
                .build());
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody().getEmail()).isEqualTo("mw310@naver.com");
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(100L);
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getAddress()).isEqualTo("Pangyo");
        assertThat(result.getBody().getNickname()).isEqualTo("minu");
    }

}