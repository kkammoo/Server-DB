package com.kh.app3.web;

import com.kh.app3.domain.bbs.Bbs;
import com.kh.app3.domain.bbs.svc.BbsSVC;
import com.kh.app3.domain.common.code.dao.CodeDAO;
import com.kh.app3.domain.common.file.UploadFile;
import com.kh.app3.domain.common.file.svc.UploadFileSVC;
import com.kh.app3.domain.common.paging.PageCriteria;
import com.kh.app3.web.form.bbs.*;
import com.kh.app3.web.form.login.LoginMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/bbs")
@RequiredArgsConstructor
public class BbsController {
  private final BbsSVC bbsSvc;
  private final CodeDAO codeDAO;
  private final UploadFileSVC uploadFileSVC;

  @Autowired
  @Qualifier("pc10")
  private PageCriteria pc;

  //게시판 코드,디코드 가져오기
  @ModelAttribute("classifier")
  public List<Code> classifier(){
    return codeDAO.code("B01");
  }


  //작성양식
  @GetMapping("/add")
//  public String addForm(Model model) {
//    model.addAttribute("addForm", new AddForm());
//    return "bbs/addForm";
//  }
  public String addForm(
      Model model,
      HttpSession session) {

    LoginMember loginMember = (LoginMember)session.getAttribute(SessionConst.LOGIN_MEMBER);

    AddForm addForm = new AddForm();
    addForm.setEmail(loginMember.getEmail());
    addForm.setNickname(loginMember.getNickname());
    model.addAttribute("addForm", addForm);

    return "bbs/addForm";
  }

  //작성처리
  @PostMapping("/add")
  public String add(
      //@Valid
      @ModelAttribute AddForm addForm,
      BindingResult bindingResult,      // 폼객체에 바인딩될때 오류내용이 저장되는 객체
      HttpSession session,
      RedirectAttributes redirectAttributes) throws IOException {
    log.info("addForm={}",addForm);

    if(bindingResult.hasErrors()){
      log.info("add/bindingResult={}",bindingResult);
      return "bbs/addForm";
    }

    Bbs bbs = new Bbs();
    BeanUtils.copyProperties(addForm, bbs);

    //세션 가져오기
    LoginMember loginMember = (LoginMember)session.getAttribute(SessionConst.LOGIN_MEMBER);
    //세션 정보가 없으면 로그인페이지로 이동
    if(loginMember == null){
      return "redirect:/login";
    }

    //세션에서 이메일,별칭가져오기
    bbs.setEmail(loginMember.getEmail());
    bbs.setNickname(loginMember.getNickname());


    Long originId = 0l;
    //파일첨부유무
    if(addForm.getFiles() == null) {
      originId = bbsSvc.saveOrigin(bbs);
    }else{
      originId = bbsSvc.saveOrigin(bbs, addForm.getFiles());
    }
    redirectAttributes.addAttribute("id", originId);
    // <=서버응답 302 get http://서버:port/bbs/10
    // =>클라이언트요청 get http://서버:port/bbs/10
    return "redirect:/bbs/{id}";
  }

  //목록
  @GetMapping("/list")
  public String list(Model model) {

      List<Bbs> list = bbsSvc.findAll();

      List<ListForm> partOfList = new ArrayList<>();
      for (Bbs bbs : list) {
        ListForm listForm = new ListForm();
        BeanUtils.copyProperties(bbs, listForm);
        partOfList.add(listForm);
      }

    model.addAttribute("list", partOfList);

    return "bbs/list";
  }

  //목록 - 요청페이지가 있는 경우
  @GetMapping("/list/{reqPage}")
  public String listAndReqPage(
      @PathVariable Integer reqPage,
      @RequestParam(required = false) String category,
      Model model) {

    //요청페이지
    pc.getRc().setReqPage(reqPage);

    List<Bbs> list = null;
    //게시물 목록 전체
    if(category == null) {
      //총레코드수
      pc.setTotalRec(bbsSvc.totalCount());
      list = bbsSvc.findAll(pc.getRc().getStartRec(), pc.getRc().getEndRec());

      //카테고리별 목록
    }else{
      pc.setTotalRec(bbsSvc.totalCount(category));
      list = bbsSvc.findAll(category, pc.getRc().getStartRec(),pc.getRc().getEndRec());
    }

    List<ListForm> partOfList = new ArrayList<>();
    for (Bbs bbs : list) {
      ListForm listForm = new ListForm();
      BeanUtils.copyProperties(bbs, listForm);
      partOfList.add(listForm);
    }

    model.addAttribute("list", partOfList);
    model.addAttribute("pc",pc);

    return "bbs/list";
  }

  //조회
  @GetMapping("/{id}")
  public String detail(
      @PathVariable Long id,
      Model model) {

    Bbs detailBbs = bbsSvc.findByBbsId(id);
    DetailForm detailForm = new DetailForm();
    BeanUtils.copyProperties(detailBbs, detailForm);
    model.addAttribute("detailForm", detailForm);

    //첨부조회
    List<UploadFile> attachFiles = uploadFileSVC.getFilesByCodeWithRid(detailBbs.getBcategory(), detailBbs.getBbsId());
    if(attachFiles.size() > 0){
      log.info("attachFiles={}",attachFiles);
      model.addAttribute("attachFiles", attachFiles);
    }

    return "bbs/detailForm";
  }

  //삭제
  @GetMapping("/{id}/del")
  public String del(
      @PathVariable Long id) {

    bbsSvc.deleteByBbsId(id);

    return "redirect:/bbs";
  }

  //수정양식
  @GetMapping("/{id}/edit")
  public String editForm(@PathVariable Long id,Model model){

    Bbs bbs = bbsSvc.findByBbsId(id);

    EditForm editForm = new EditForm();
    BeanUtils.copyProperties(bbs,editForm);
    model.addAttribute("editForm", editForm);

    //첨부조회
    List<UploadFile> attachFiles = uploadFileSVC.getFilesByCodeWithRid(bbs.getBcategory(), bbs.getBbsId());
    if(attachFiles.size() > 0){
      log.info("attachFiles={}",attachFiles);
      model.addAttribute("attachFiles", attachFiles);
    }

    return "bbs/editForm";
  }

  //수정처리
  @PostMapping("/{id}/edit")
  public String edit(@PathVariable Long id,
                     @Valid @ModelAttribute EditForm editForm,
                     BindingResult bindingResult,
                     RedirectAttributes redirectAttributes
  ) {

    if(bindingResult.hasErrors()){
      return "bbs/editForm";
    }

    Bbs bbs = new Bbs();
    BeanUtils.copyProperties(editForm, bbs);
    bbsSvc.updateByBbsId(id,bbs);

    if(editForm.getFiles() == null) {
      bbsSvc.updateByBbsId(id, bbs);
    }else{
      bbsSvc.updateByBbsId(id, bbs, editForm.getFiles());
    }
    redirectAttributes.addAttribute("id",id);

    return "redirect:/bbs/{id}";
  }

  //답글작성양식
  @GetMapping("/{id}/reply")
  public String replyForm(@PathVariable Long id,
                          Model model,HttpSession session) {
    Bbs parentBbs = bbsSvc.findByBbsId(id);
    ReplyForm replyForm = new ReplyForm();
    replyForm.setBcategory(parentBbs.getBcategory());
    replyForm.setTitle("답글:"+parentBbs.getTitle());

    //세션에서 로그인정보 가져오기
    LoginMember loginMember = (LoginMember)session.getAttribute(SessionConst.LOGIN_MEMBER);
    replyForm.setEmail(loginMember.getEmail());
    replyForm.setNickname(loginMember.getNickname());

    model.addAttribute("replyForm", replyForm);
    return "bbs/replyForm";
  }

  //답글작성처리
  @PostMapping("/{id}/reply")
  public String reply(
      @PathVariable Long id,      //부모글의 bbsId
      @Valid ReplyForm replyForm,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes
  ){
    if(bindingResult.hasErrors()){
      return "bbs/replyForm";
    }

    Bbs replyBbs = new Bbs();
    BeanUtils.copyProperties(replyForm, replyBbs);

    //부모글의 bcategory,bbsId,bgroup,step,bindent 참조
    appendInfoOfParentBbs(id, replyBbs);

    //답글저장(return 답글번호)
    Long replyBbsId = bbsSvc.saveReply(id, replyBbs);

    redirectAttributes.addAttribute("id",replyBbsId);
    return "redirect:/bbs/{id}";
  }

  //부모글의 bbsId,bgroup,step,bindent
  private void appendInfoOfParentBbs(Long parentBbsId, Bbs replyBbs) {
    Bbs parentBbs = bbsSvc.findByBbsId(parentBbsId);
    replyBbs.setBcategory(parentBbs.getBcategory());
    replyBbs.setPbbsId(parentBbs.getBbsId());
    replyBbs.setBgroup(parentBbs.getBgroup());
    replyBbs.setStep(parentBbs.getStep());
    replyBbs.setBindent(parentBbs.getBindent());
  }
}