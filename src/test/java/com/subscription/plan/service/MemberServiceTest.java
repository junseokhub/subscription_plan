package com.subscription.plan.service;

import com.subscription.plan.domain.Member;
import com.subscription.plan.dto.MemberChangeNameRequestDto;
import com.subscription.plan.dto.MemberResponseDto;
import com.subscription.plan.dto.MemberSignUpRequestDto;
import com.subscription.plan.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("Find Member")
    void findMember_success() {
        Member member = Member.builder().userName("testUser").build();
        given(memberRepository.findByUserName("testUser")).willReturn(Optional.of(member));

        MemberResponseDto response = memberService.findMember("testUser");

        assertThat(response.userName()).isEqualTo("testUser");
    }

    @Test
    @DisplayName("Success Sign Up")
    void signUp_success() {
        MemberSignUpRequestDto dto = new MemberSignUpRequestDto("testUser");
        given(memberRepository.findByUserName("testUser")).willReturn(Optional.empty());
        given(memberRepository.save(any(Member.class))).willAnswer(invocation -> invocation.getArgument(0));

        MemberResponseDto response = memberService.saveMember(dto);

        assertThat(response.userName()).isEqualTo("testUser");
    }

    @Test
    @DisplayName("Duplicate Member")
    void signUp_duplicateMember() {
        Member member = Member.builder().userName("testUser").build();
        given(memberRepository.findByUserName("testUser")).willReturn(Optional.of(member));

        MemberSignUpRequestDto dto = new MemberSignUpRequestDto("testUser");

        assertThatThrownBy(() -> memberService.saveMember(dto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 존재하는 회원입니다.");

    }

    @Test
    @DisplayName("Change Member UserName")
    void changeUserName_success() {
        Member member = Member.builder()
                .userName("old")
                .build();

        given(memberRepository.findByUserName("old"))
                .willReturn(Optional.of(member));

        MemberChangeNameRequestDto dto = new MemberChangeNameRequestDto("old", "new");

        MemberResponseDto response = memberService.changeUserName(dto);

        assertThat(response.userName()).isEqualTo("new");
    }

    @Test
    @DisplayName("Delete Member")
    void deleteUser_success() {
        Member member = Member.builder().userName("testUser").build();
        given(memberRepository.findByUserName("testUser")).willReturn(Optional.of(member));

        memberService.deleteUser("testUser");

        verify(memberRepository).delete(member);
    }
}