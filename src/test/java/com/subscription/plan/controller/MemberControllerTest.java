package com.subscription.plan.controller;

import com.subscription.plan.dto.MemberResponseDto;
import com.subscription.plan.dto.MemberSignUpRequestDto;
import com.subscription.plan.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MemberService memberService;

    @Test
    @DisplayName("회원 가입 요청 시 성공하면 200 OK와 회원 정보를 반환한다.")
    void signUpTest() throws Exception {
        MemberSignUpRequestDto requestDto = new MemberSignUpRequestDto("tester");
        MemberResponseDto responseDto = new MemberResponseDto(1L, "tester");

        given(memberService.saveMember(any(MemberSignUpRequestDto.class)))
                .willReturn(responseDto);

        mockMvc.perform(post("/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("tester"))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("사용자 이름으로 조회 시 정보를 반환한다.")
    void getMemberTest() throws Exception {
        MemberResponseDto responseDto = new MemberResponseDto(1L, "tester");
        given(memberService.findMember("tester")).willReturn(responseDto);

        mockMvc.perform(get("/member/find")
                        .param("username", "tester"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("tester"));
    }
}