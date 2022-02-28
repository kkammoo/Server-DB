package com.kh.app3.web.form.notice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

  private Long noticeId;
  private String subject;
  private String cdate;
  private String ctime;
  private Long hit;

}
