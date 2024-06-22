package com.example.demo.controller;

import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.UserEntity;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 사용자는_특정_유저의_정보를_개인정보는_소거된채_전달_받을_수_있다() throws Exception{
        //given
        //when
        //then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("mw310@naver.com"))
                .andExpect(jsonPath("$.nickname").value("manu"))
                .andExpect(jsonPath("$.address").doesNotExist())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }
    @Test
    void 사용자는_존재하지_않는_유저의_아이디로_Api_호출할_경우_404_응답을_받는다() throws Exception{
        //given
        //when
        //then
        mockMvc.perform(get("/api/users/1123132121"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Users에서 ID 1123132121를 찾을 수 없습니다."));

    }
    @Test
    void 사용자는_인증_코드로_계정을_활성화_시킬_수_있다() throws Exception{
        //given
        //when
        //then
        mockMvc.perform(get("/api/users/2/verify").queryParam("certificationCode", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"))
                .andExpect(status().isFound());

        UserEntity userEntity = userRepository.findById(2L).get();
        Assertions.assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }
    @Test
    void 시용자는_내_정보를_불러올_떄_개인정보인_주소도_갖고_올_수_있다() throws Exception{
        //given
        //when
        //then
        mockMvc.perform(get("/api/users/me").header("EMAIL", "mw310@naver.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("mw310@naver.com"))
                .andExpect(jsonPath("$.nickname").value("manu"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.address").value("Seoul"));;
    }
    @Test
    void 시용자는_내_정보를_수정할_수_있다() throws Exception{
        //given
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .nickname("manu-4")
                .address("Pangyo")
                .build();
        //when

        //then
        mockMvc.perform(put("/api/users/me").header("EMAIL", "mw310@naver.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("mw310@naver.com"))
                .andExpect(jsonPath("$.nickname").value("manu-4"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.address").value("Pangyo"));;
    }

}