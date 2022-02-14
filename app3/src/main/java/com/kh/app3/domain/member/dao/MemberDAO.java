package com.kh.app3.domain.member.dao;

import com.kh.app3.domain.member.Member;

import java.util.List;

public interface MemberDAO {
  //가입
  Member insertMember(Member member);

  //수정
  void updateMember(Member member);

  //조회 by email
  Member selectMemberByEmail(String email);

  //조회 by member_id
  Member selectMemberByMemberId(Long memberId);

  //전체조회
  List<Member> selectAll();
  
  //탈퇴
  void deleteMember(String email);

  //회원 유무체크
  boolean isMember(String email);

  //로그인 인증
  Member login(String email, String passwd);
}
