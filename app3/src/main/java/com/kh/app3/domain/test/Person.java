package com.kh.app3.domain.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
@Component
public class Person {
  private String name;
  private int age;

  public void smile() {
    log.info(name + "가 웃다");
  }

  public void study() {
    log.info(name + "가 공부하다");
  }

}
