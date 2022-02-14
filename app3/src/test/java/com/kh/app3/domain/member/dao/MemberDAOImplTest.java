package com.kh.app3.domain.member.dao;

import com.kh.app3.domain.member.Member;
import com.kh.app3.domain.member.dao.MemberDAO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.assertj.core.api.Assertions;

import java.util.List;

@Slf4j
@SpringBootTest  // springboot 환경에서 테스트 진행
public class MemberDAOImplTest {

  @Autowired // SC(스프링 컨테이너)에서 동일 타입의 객체를 찾아서 주입시켜 준다.
  private MemberDAO memberDAO;

  @Test       // 테스트 대상
  @DisplayName("등록") // 테스트 케이스 이름
//  @Disabled  테스트 대상에서 제외
  void insert(){
    Member member = new Member(null,"test3@kh.com","1234","테스터abc");
    Member savedMember = memberDAO.insertMember(member);
//    log.info("savedMember.getEmail={}",savedMember.getEmail());
//    log.info("savedMember.getPasswd={}",savedMember.getPasswd());
//    log.info("savedMember.getNickName={}",savedMember.getNickname());
    savedMember.setMemberId(null);
    Assertions.assertThat(savedMember).usingRecursiveComparison().isEqualTo(member);
  };

  @Test
  @DisplayName("조회")
  void selectByMemberId(){
    Long memberId = 21L;
    Member member = memberDAO.selectMemberByMemberId(memberId);
    log.info("member={}", member);
  }

  @Test
  @DisplayName("수정")
  void updateMember() {

    String email = "test6@test.com";
    String nickname = "테스터6";
    Member member = new Member(null, email, null, nickname);
    memberDAO.updateMember(member);

    Member updatedMember = memberDAO.selectMemberByEmail(email);

    Assertions.assertThat(updatedMember.getNickname()).isEqualTo(nickname);
  }

  @Test
  @DisplayName("회원탈퇴")
  void deleteMember() {
    String email = "test7@test.com";
    memberDAO.deleteMember(email);

    boolean isMember = memberDAO.isMember(email);
    Assertions.assertThat(isMember).isFalse();
  }

  @Test
  @DisplayName("전체조회")
  void selectAll(){

    List<Member> members = memberDAO.selectAll();
//    log.info("members={}", members);
    Assertions.assertThat(members.size()).isEqualTo(6);
  }

  @Test
  @DisplayName("로그인")
  void login () {

    String email = "test1@kh.com";
    String passwd = "1234";
    Member member = memberDAO.login(email, passwd);

    Assertions.assertThat(member).isNotNull();
  }
}
