package com.example.demo;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoApplicationTests {
  @Autowired
  private TestRestTemplate testRestTemplate;
  private HttpEntity<Object> entity;

  @Before
  public void setUp() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setAccept(Collections.singletonList(MediaType.TEXT_HTML));
    this.entity = new HttpEntity<>(httpHeaders);
  }

  @Test
  public void shouldGetIndexHtml() {
    Assertions.assertThat(this.testRestTemplate
            .exchange("/public/index.html", HttpMethod.GET, this.entity, String.class).getBody())
            .contains("static file");
  }

  @Test
  public void shouldGetIndexHtmlAsPublicRoot() {
    Assertions.assertThat(this.testRestTemplate
            .exchange("/public/", HttpMethod.GET, this.entity, String.class).getBody())
            .contains("static file");
  }

  @Test
  public void shouldGetHashUrlAsIndexHtml() {
    Assertions.assertThat(this.testRestTemplate
            .exchange("/public/#/routeA", HttpMethod.GET, this.entity, String.class).getBody())
            .contains("static file");
  }

}
