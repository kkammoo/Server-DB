package com.kh.app3.domain.bbs.dao;

import com.kh.app3.domain.bbs.Bbs;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
class BbsDAOImplTest {

  @Autowired
  private BbsDAO bbsDAO;

  @Test
  @DisplayName("원글 작성")
  void saveOrigin() {
    Bbs bbs = new Bbs();

    bbs.setBcategory("B0101");
    bbs.setTitle("제목3");
    bbs.setEmail("test1@kh.com");
    bbs.setNickname("테스터1");
    bbs.setBcontent("본문1");

    Long saveOriginId = bbsDAO.saveOrigin(bbs);
    Assertions.assertThat(saveOriginId).isEqualTo(4);
    log.info("saveOriginId={}", saveOriginId);
  }

  @Test
  @DisplayName("답글작성")
  void saveReply() {
    Long pbbsId = 2L;
    Bbs bbs = new Bbs();
    bbs.setBcategory("B0101");
    bbs.setTitle("제목1-2");
    bbs.setEmail("test1@kh.com");
    bbs.setNickname("테스터1");
    bbs.setBcontent("본문1");

    bbsDAO.saveReply(pbbsId, bbs);
  }

  @Test
  @DisplayName("목록")
  void findAll() {
    List<Bbs> list = bbsDAO.findAll();

    Assertions.assertThat(list.size()).isEqualTo(3);
    Assertions.assertThat(list.get(0).getTitle()).isEqualTo("제목1");
    for (Bbs bbs : list) {
      log.info(bbs.toString());
    }
  }

  @Test
  @DisplayName("목록(페이징)")
  void findAllWithPaging(){
    List<Bbs> list = bbsDAO.findAll(11, 20);

    Assertions.assertThat(list.size()).isEqualTo(10);
  }

  @Test
  @DisplayName("목록(카테고리&페이징)")
  void findAllWithCategoryAndPaging(){
    List<Bbs> list = bbsDAO.findAll("B0101",11, 20);

    Assertions.assertThat(list.size()).isEqualTo(10);
  }

  @Test
  @DisplayName("카테고리별 목록")
  void findAllByCategory() {
    String category = "B0104";
    List<Bbs> list = bbsDAO.findAll(category);

    for (Bbs bbs : list) {
      log.info(bbs.toString());
    }
  }

  @Test
  @DisplayName("게시글 단건 조회")
  void findByBbsId() {
    Long bbsId = 3L;
    Bbs findedBbsItem = bbsDAO.findByBbsId(bbsId);
    Assertions.assertThat(findedBbsItem.getTitle()).isEqualTo("제목1");
  }

  @Test
  @DisplayName("게시글 단건 삭제")
  void deleteByBbsId() {
    Long bbsId = 3L;
    int deletedBbsItemCount = bbsDAO.deleteByBbsId(bbsId);

    Assertions.assertThat(deletedBbsItemCount).isEqualTo(1);

    Bbs findedBbsItem = bbsDAO.findByBbsId(bbsId);
    Assertions.assertThat(findedBbsItem).isNull();
  }

  @Test
  @DisplayName("게시글 수정")
  void updateByBbsId() {

    Long bbsId = 2L;
    //수정전
    Bbs beforeUpdatingItem = bbsDAO.findByBbsId(bbsId);
    beforeUpdatingItem.setBcategory("B0102");
    beforeUpdatingItem.setTitle("수정후 제목");
    beforeUpdatingItem.setBcontent("수정후 본문");
    bbsDAO.updateByBbsId(bbsId, beforeUpdatingItem);

    //수정후
    Bbs afterUpdatingItem = bbsDAO.findByBbsId(bbsId);

    //수정전후 비교
    Assertions.assertThat(beforeUpdatingItem.getBcategory())
        .isEqualTo(afterUpdatingItem.getBcategory());
    Assertions.assertThat(beforeUpdatingItem.getTitle())
        .isEqualTo(afterUpdatingItem.getTitle());
    Assertions.assertThat(beforeUpdatingItem.getBcontent())
        .isEqualTo(afterUpdatingItem.getBcontent());
    Assertions.assertThat(beforeUpdatingItem.getUdate())
        .isNotEqualTo(afterUpdatingItem.getUdate());
  }

  @Test
  @DisplayName("조회수 증가")
  void increaseHitCount() {
    Long bbsId = 1L;
    //조회전 조회수
    int beforeHitCount = bbsDAO.findByBbsId(bbsId).getHit();
    //조회
    bbsDAO.increaseHitCount(1L);
    //조회후 조회수
    int afterHitCount = bbsDAO.findByBbsId(bbsId).getHit();
    //조회후 조회수 - 조회전 조회수 = 1
    Assertions.assertThat(afterHitCount - beforeHitCount).isEqualTo(1);
  }

  @Test
  @DisplayName("전체건수")
  void totalCount() {

    int size = bbsDAO.findAll().size();
    int i = bbsDAO.totalCount();

    Assertions.assertThat(i).isEqualTo(size);
  }

  @Test
  @DisplayName("다수의 원글 작성")
  void saveOrigins() {

    for (int i = 0; i < 350; i++) {

      Bbs bbs = new Bbs();

      bbs.setBcategory("B0101");
      bbs.setTitle("제목" + i);
      bbs.setEmail("test1@kh.com");
      bbs.setNickname("테스터1");
      bbs.setBcontent("본문" + i);

      Long saveOriginId = bbsDAO.saveOrigin(bbs);
    }
  }
}
