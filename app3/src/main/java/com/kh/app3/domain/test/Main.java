package com.kh.app3.domain.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
  public static void main(String[] args) {

    Person p1 = new Person("홍길동", 30);
    log.info(p1.getName());
    p1.study();
    p1.smile();





  }
}
