package com.kh.app3.web;

import com.kh.app3.domain.common.paging.RecordCriteria;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class PagingConfigTest {

  @Autowired
  @Qualifier("rc10")
  RecordCriteria recordCriteria_10;

  @Autowired
  @Qualifier("rc5")
  RecordCriteria recordCriteria_5;

  @Test
  @DisplayName("레코드 계산")
  void calRec() {

    log.info("시작레코드_10={}", recordCriteria_10.getStartRec());
    log.info("종료레코드_10={}", recordCriteria_10.getEndRec());

    log.info("시작레코드_5={}", recordCriteria_5.getStartRec());
    log.info("종료레코드_5={}", recordCriteria_5.getEndRec());
  }
}