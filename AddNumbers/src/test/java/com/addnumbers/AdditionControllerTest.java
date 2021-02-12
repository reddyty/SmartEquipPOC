package com.addnumbers;

import com.addnumbers.controller.AdditionController;
import com.addnumbers.service.AdditionService;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AdditionControllerTest {


    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Autowired
    private AdditionService additionService;
    @Autowired
    private AdditionController additionController;

    private static final String SECRET_KEY = "M@r1@b$";

    @Test
    void testGetClientMsg(){
        assertEquals("Hey Service, can you provide me a question with numbers to add?",additionController.getClientMsg());
    }

    @Test
    void testGetServiceMsg(){
        when(request.getHeader("Authorization")).thenReturn("BearerTest");
        ResponseEntity<String> responseEntity = additionController.getServiceMsg(request, response);
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }


    @Test
    void testGetServiceMsgWithNullRequestHeader(){
        when(request.getHeader("Authorization")).thenReturn(null);
        ResponseEntity<String> responseEntity = additionController.getServiceMsg(request,response);
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void testGetServiceMsgWithDifferAuthorizationRequestHeader(){
        when(request.getHeader("Authorization")).thenReturn("MockTest");
        ResponseEntity<String> responseEntity = additionController.getServiceMsg(request,response);
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }
    @Test
    void testGetServiceMsgWithoutRequestHeaders(){
        ResponseEntity<String> responseEntity = additionController.getServiceMsg(request,response);
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }


    @Test
    void testFinalMessage(){
        List<Object> questions = new ArrayList<>(Arrays.asList(10,20,30,40,50));
        additionController.setAdditionService(additionService);
        List<Integer> values = new ArrayList<>(Arrays.asList(10,20,30,40,50));
        int total =  150;
        String question = questions.stream().map(x->x.toString()).collect(Collectors.joining());
        String token= generateToken(question,SECRET_KEY,300000);
        when(request.getHeader("token")).thenReturn(token);
        ResponseEntity<String> response = additionController.finalMessage(values, total, request);
        assertNotNull(response);
        assertEquals(response.getStatusCode(),HttpStatus.OK);
    }

    @Test
    void testFinalMessageWithManipulatedQuestions(){
        additionController.setAdditionService(additionService);
        List<Integer> values = new ArrayList<>(Arrays.asList(10,20,30,40,500));
        int total = 150;
        String question = values.stream().map(x->x.toString()).collect(Collectors.joining());
        String token= generateToken(question,SECRET_KEY,300000);
        when(request.getHeader("token")).thenReturn(token);
        ResponseEntity<String> response = additionController.finalMessage(values, total, request);
        assertNotNull(response);
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
    }

    private String generateToken(String questions, String signingKey, long tokenExpirationTime){

        long nowMillis = System.currentTimeMillis();
        Date currentDate = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(currentDate)
                .setSubject(questions)
                .signWith(SignatureAlgorithm.HS512, signingKey);

        if (tokenExpirationTime > 0) {
            long expMillis = nowMillis + tokenExpirationTime;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }
}
