package com.example.demo.user.service;

import com.example.demo.mock.FakeMailSender;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CertificationServiceImplTest {

    @Test
    public void 이메일과_컨텐츠가_제대로_만들어져서_보내지는지_테스트한다() {

        //given
        FakeMailSender fakeMailSender = new FakeMailSender();
        CertificationService certificationServiceImpl = new CertificationService(fakeMailSender);
        //when
        certificationServiceImpl.send("mw310@naver.com", 1, "aaaadsaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

        //then
        assertThat(fakeMailSender.email).isEqualTo("mw310@naver.com");
        assertThat(fakeMailSender.title).isEqualTo("Please certify your email address");
        assertThat(fakeMailSender.content).isEqualTo("Please click the following link to certify your email address: http://localhost:8080/api/users/1/verify?certificationCode=aaaadsaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }
}