package com.example.demo.user.infrastructure;

import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Sql("/sql/user-repository-test-data.sql")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Test
    void findByIdAndStatus_로_유저데이터를_찾아올_수_있다() throws Exception{

        Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.ACTIVE);

        //then
        assertThat(result.isPresent()).isTrue();

    }
    @Test
    void findByIdAndStatus는_데이터가_없으면_Optional_empty_를_내려준다() throws Exception{

        Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.PENDING);

        //then
        assertThat(result.isPresent()).isFalse();
        assertThat(result.isEmpty()).isTrue();

    }
    @Test
    void findByEmailAndStatus_로_유저데이터를_찾아올_수_있다() throws Exception{

        Optional<UserEntity> result = userRepository.findByEmailAndStatus("mw310@naver.com", UserStatus.ACTIVE);

        //then
        assertThat(result.isPresent()).isTrue();

    }
    @Test
    void findByEmailAndStatus는_데이터가_없으면_Optional_empty_를_내려준다() throws Exception{

        Optional<UserEntity> result = userRepository.findByEmailAndStatus("mw310@naver.com", UserStatus.PENDING);

        //then
        assertThat(result.isPresent()).isFalse();
        assertThat(result.isEmpty()).isTrue();

    }
}