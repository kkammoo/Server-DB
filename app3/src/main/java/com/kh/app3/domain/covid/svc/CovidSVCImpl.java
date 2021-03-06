package com.kh.app3.domain.covid.svc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Slf4j
@Service
public class CovidSVCImpl implements CovidSVC {

  @Override
  public Response getCovidInfo(String sdate, String edate) {

    Response res = null;
//    String sdate = startDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//    String edate = endDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

    try {
      StringBuilder urlBuilder = new StringBuilder("http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19GenAgeCaseInfJson"); /*URL*/
      urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=9BAQGdznCtj5LPEpxjo34q5v1T2TQ48rBcwDWJuLAUukzjx7WaHsG%2B7%2Fuz5XhfJ6MOnbDIIz%2F%2BRJvY%2BauB%2FN3A%3D%3D"); /*Service Key*/
      urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
      urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
      urlBuilder.append("&" + URLEncoder.encode("startCreateDt", "UTF-8") + "=" + URLEncoder.encode(sdate, "UTF-8")); /*검색할 생성일 범위의 시작*/
      urlBuilder.append("&" + URLEncoder.encode("endCreateDt", "UTF-8") + "=" + URLEncoder.encode(edate, "UTF-8")); /*검색할 생성일 범위의 종료*/
      URL url = new URL(urlBuilder.toString());
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Content-type", "application/json");
      System.out.println("Response code: " + conn.getResponseCode());
      BufferedReader rd;
      if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
        rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      } else {
        rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
      }
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = rd.readLine()) != null) {
        sb.append(line);
      }
      rd.close();
      conn.disconnect();
      System.out.println(sb.toString());

      res = parser(sb.toString());
      log.info("res={}", res);
      log.info("totalCount={}", res.getBody());
    } catch (Exception e) {
      e.printStackTrace();
    }

    return res;
  }

  private Response parser(String xmlStr) {
    ObjectMapper xmlMapper = new XmlMapper();
    Response response = null;
    try {
      response = xmlMapper.readValue(xmlStr, Response.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return response;
  }
}