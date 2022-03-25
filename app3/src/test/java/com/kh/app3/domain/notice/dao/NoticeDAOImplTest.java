package com.kh.app3.domain.notice.dao;

import com.kh.app3.domain.notice.Notice;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class NoticeDAOImplTest {

  @Autowired // SC(스프링 컨테이너)에서 동일 타입의 객체를 주입받는다.
  private NoticeDAO noticeDAO;

  @Test
  @DisplayName("공지사항 등록")
  void create() {

    Notice notice = new Notice();
    notice.setSubject("제목4");
    notice.setContent("본문4");
    notice.setAuthor("홍길동");
    notice.setNoticeId(null);
    notice.setHit(null);
    notice.setCdate(null);
    notice.setUtade(null);

    Notice savedNotice = noticeDAO.create(notice);

    Assertions.assertThat(notice.getSubject()).isEqualTo(savedNotice.getSubject());
    log.info("savedNoticeId={}", savedNotice.getNoticeId());
  }

  @Test
  @DisplayName("공지사항 전체조회")
  void selectAll() {

    List<Notice> notices = noticeDAO.selectAll();
    Assertions.assertThat(notices.size()).isEqualTo(3);
  }


  @Test
  @DisplayName("공지사항 조회 1건")
  void selectOne() {
    Long noticeId = 21L;
    Notice notice = noticeDAO.selectOne(noticeId);
    Assertions.assertThat(notice).isNotNull();
    log.info("notice={}", notice);
  }

  @Test
  @DisplayName("공지사항 변경")
  void update() {
    // when
    Long noticeId = 21L;
    Notice notice = noticeDAO.selectOne(noticeId);
    notice.setSubject("수정 후 제목");
    notice.setContent("수정 후 내용");

    // try
    Notice updatedNotice = noticeDAO.update(notice);

    // then
    Assertions.assertThat(updatedNotice.getSubject()).isEqualTo(notice.getSubject());
    Assertions.assertThat(updatedNotice.getContent()).isEqualTo(notice.getContent());

    log.info("notice={}", notice);
  }

  @Test
  @DisplayName("공지 사항 삭제 by 공지ID")
  void delete() {
    // when
    Long noticeId = 21L;

    // try
    int cnt = noticeDAO.delete(noticeId);

    // then
//    Assertions.assertThat(cnt).isEqualTo(1);
    Assertions.assertThat(noticeDAO.selectOne(21L)).isNull();

  }

  @Test
  @DisplayName("조회수 증가")
  void updateHit() {
    // when
    Long noticeId = 4L;
    Notice notice = noticeDAO.selectOne(noticeId);
    Long currentHit = notice.getHit(); // 수정 전 조회수

    // try
    noticeDAO.updateHit(notice.getNoticeId());

    // then
    Notice noticeAfterHitting = noticeDAO.selectOne(noticeId); // 수정 후 조회수
    Assertions.assertThat(noticeAfterHitting.getHit()).isEqualTo(currentHit + 1);


  }

}
