package com.subocol.manage.purchase.integration;

import com.subocol.manage.purchase.application.dtos.ManageNoticeMMDTO;
import com.subocol.manage.purchase.application.dtos.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.*;
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ManageNoticeMMIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @Order(1)
    @Disabled
    void ManageNoticeMMIntegrationTest() throws Exception {
        //Given-preconditions

        Long noticeId=28753L;
        ManageNoticeMMDTO manageNoticeMMDTO=new ManageNoticeMMDTO().setNoticeId(noticeId);
        //when-action to do

        ResponseEntity<ResponseDTO> apiResponse=testRestTemplate.postForEntity("http://localhost:8080/api/managenotice/mm", manageNoticeMMDTO, ResponseDTO.class);
        //        log.info(apiResponse.getBody().toString());
        //then-verify result
        Assertions.assertEquals(HttpStatus.OK, apiResponse.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_JSON, apiResponse.getHeaders().getContentType());

        ResponseDTO responseBody=apiResponse.getBody();

        assert responseBody != null;

        Assertions.assertEquals(responseBody.getStatus(), HttpStatus.OK.value());
        Assertions.assertTrue(responseBody.isSuccess());
    }



}
