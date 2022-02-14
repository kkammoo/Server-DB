package com.kh.app3.web.form;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data // getter, setter, toString, hashCode 등등을 만들어줌
public class LoginForm {

  @NotBlank //null이 아니고, 적어도 공백문자가 아닌 문자가 1개 이상인지 체크.
  @Email // email형식인지 체크
  @Size(min=4, max=50)
  private String email;

  @NotBlank
  @Size(min=4, max=8)
  private String passwd;


}
