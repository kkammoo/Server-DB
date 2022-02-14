package com.kh.app3.web;


import com.kh.app3.domain.member.Member;
import com.kh.app3.domain.member.svc.MemberSVC;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

  private final MemberSVC memberSVC;

//  @Autowired
//  public AdminController(MemberSVC memberSVC) {
//    this.memberSVC = memberSVC;
//  }

  //관리자 홈
  @GetMapping
  public String home(){

    return "admin/home";
  }

  //회원 전체조회
  @GetMapping("/members")
  public String members(Model model) {

    List<Member> memberAll = memberSVC.findAll();
    model.addAttribute("memberAll", memberAll);
    return "admin/member/members";
  }

  //회원 개별조회
  @GetMapping("/members/{email}")
  public String member(
          @PathVariable("email") String email,
          Model model) { //@PathVariable : url경로상의 값을 가져올 때 사용.

    Member findedMember = memberSVC.findByEmail(email);
    model.addAttribute("member", findedMember);
    return "admin/member/member";
  }

  //회원탈퇴
  @GetMapping("members/{email}/del")
  public String out(@PathVariable("email") String email) {

    memberSVC.out(email);

    return "redirect:/admin/members"; //정보 수정, 삭제를 한 뒤엔 redirect를 이용한다.
  }
}
