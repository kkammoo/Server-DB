package com.kh.app3.domain.covid.svc;

public interface CovidSVC {
  Response getCovidInfo(String startDate, String endDate);
}