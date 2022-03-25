package com.kh.app3.domain.bbs.svc;

import com.kh.app3.domain.bbs.Bbs;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BbsSVC {
  /**
   * 원글작성 - 첨부파일이 없는 경우
   * @param bbs
   * @return 게시글 번호
   */
  Long saveOrigin(Bbs bbs);

  /**
   * 원글작성 - 첨부파일이 있는 경우
   * @param bbs
   * @param files
   * @return
   */
  Long saveOrigin(Bbs bbs, List<MultipartFile> files);

  /**
   * 목록
   * @return
   */
  List<Bbs> findAll();
  List<Bbs>  findAll(int startRec, int endRec);
  List<Bbs>  findAll(String category,int startRec, int endRec);

  /**
   * 게시물 조회
   * @param id 게시물 id
   * @return
   */
  Bbs findByBbsId(Long id);

  /**
   * 삭제
   * @param id 게시물 id
   * @return 삭제건수 (반환되는 삭제건수를 확인함으로써 삭제가 이루어졌는지 알 수 있음)
   */
  int deleteByBbsId(Long id);

  /**
   * 수정
   * @param id 게시물 id
   * @param bbs 수정 내용
   * @return 수정건수
   */
  int updateByBbsId(Long id, Bbs bbs);

  /**
   * 수정-첨부
   * @param id 게시글 번호
   * @param bbs 수정내용
   * @param files 첨부파일
   * @return 수정건수
   */
  int updateByBbsId(Long id,Bbs bbs, List<MultipartFile> files);

  /**
   * 답글 작성
   * @param pbbsId 부모글 번호
   * @param replyBbs
   * @return
   */
  Long saveReply(Long pbbsId, Bbs replyBbs);

  /**
   * 전체 건수
   * @return
   */
  int totalCount();
  int totalCount(String bcategory);
}
