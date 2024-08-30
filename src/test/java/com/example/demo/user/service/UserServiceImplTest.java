package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;


class UserServiceImplTest {
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void init() {
        FakeMailSender fakeMailSender = new FakeMailSender();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();

        this.userServiceImpl = UserServiceImpl.builder()
                .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                .clockHolder(new TestClockHolder(1679530673958L))
                .userRepository(fakeUserRepository)
                .certificationService(new CertificationService(fakeMailSender))
                .build();
        fakeUserRepository.save(User.builder()
                .id(1L)
                .email("mw310@naver.com")
                .nickname("manu")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                 .build());
        fakeUserRepository.save(User.builder()
                .id(2L)
                .email("mw410@naver.com")
                .nickname("manu1")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaB")
                 .build());

    }

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다() throws Exception{
        //given
        String email = "mw310@naver.com";

        //when
        User result = userServiceImpl.getByEmail(email);
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
            User result = userServiceImpl.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }
    @Test
    void getById은_ACTIVE_상태인_유저를_찾아올_수_있다() throws Exception{
        //given
        //when
        User result = userServiceImpl.getById(1);
        //then
        assertThat(result.getNickname()).isEqualTo("manu");
    }
    @Test
    void getById은_PENDING_상태인_유저를_찾아올_수_없다() throws Exception{
        //given
        //when
        //then
        assertThatThrownBy(() -> {
            User result = userServiceImpl.getById(2);
        }).isInstanceOf(ResourceNotFoundException.class);
    }
    @Test
    void userCreateDto_를_이용하여_유저를_생성할_수_있다() throws Exception{
        //given
        UserCreate userCreate = UserCreate.builder()
                .email("mw310@kakao.com")
                .address("Gyeongi")
                .nickname("manu2")
                .build();
        //when
        User result = userServiceImpl.create(userCreate);
        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }
    @Test
    void userUpdateDto_를_이용하여_유저를_수정할_수_있다() throws Exception{
        //given
        UserUpdate userUpdate = UserUpdate.builder()
                .address("Incheon")
                .nickname("manu3")
                .build();
        //when
        userServiceImpl.update(1, userUpdate);
        //then
        User user = userServiceImpl.getById(1);
        assertThat(user.getId()).isNotNull();
        assertThat(user.getAddress()).isEqualTo("Incheon");
        assertThat(user.getNickname()).isEqualTo("manu3");
    }
    @Test
    void user를_로그인_시키면_마지막_로그인_시간이_변경된다() throws Exception{
        //when
        userServiceImpl.login(1);

        //then
        User result = userServiceImpl.getById(1);
        assertThat(result.getLastLoginAt()).isEqualTo(1679530673958L);
    }
    @Test
    void PENDING_상태의_사용자는_인증_코드를_ACTIVE_시킬_수_있다() throws Exception{
        //when
        String uuid = "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaB";
        userServiceImpl.verifyEmail(2, uuid);

        //then
        User result = userServiceImpl.getById(2);
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }
    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() throws Exception{
        //given
        // when
        String uuid = "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac";
        //then
        assertThatThrownBy(() -> {
            userServiceImpl.verifyEmail(2, uuid);
        }).isInstanceOf(CertificationCodeNotMatchedException.class);

    }


}