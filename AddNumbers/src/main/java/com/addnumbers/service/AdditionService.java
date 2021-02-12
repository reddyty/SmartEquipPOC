package com.addnumbers.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AdditionService {

    private static String serviceRsltMsg = ": Great. The original question was “Please sum the numbers :” ";
    private static String correctAnswerMSG = "That’s great";
    private static String wrongAnswerMSG = "That’s wrong. Please try again";
    private static String clientMsg = "Hey Service, can you provide me a question with numbers to add?";
    private static final String SECRET_KEY = "M@r1@b$";


    public String getClientMsg(){
        return clientMsg;
    }

    public String getServiceMsg() {
        List<Object> randomNumber = getRandomNumbers();
        String tokenValue = randomNumber.stream().map(x->x.toString()).collect(Collectors.joining());
        String tokenStr = generateToken(tokenValue, SECRET_KEY,300000);
        return serviceRsltMsg + Arrays.deepToString(randomNumber.toArray())+"token:"+tokenStr;
    }

    public ResponseEntity<String> finalMessage(List<Integer> question, int total, String token) {
        String tokenBody = decodeToken(token,SECRET_KEY);
        Integer sumTotal = sum(question);
        String questionToken = question.stream().map(x->x.toString()).collect(Collectors.joining());
        if(sumTotal == total && token!=null && questionToken.equals(tokenBody))
        {
            return ResponseEntity.ok(getFileMessageSuccessMsg());
        } else {
            return new ResponseEntity(getFinalMessageErrorMsg(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    private String getFileMessageSuccessMsg(){
        return correctAnswerMSG;
    }

    private String getFinalMessageErrorMsg(){
        return wrongAnswerMSG;
    }

    public Integer sum(List<Integer> values) {
        return values.stream().reduce(0, (a, b) -> a + b);
    }

    public List<Object> getRandomNumbers() {
        return new Random().ints(0, 350).limit(5).boxed().collect(Collectors.toList());
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

    public static String decodeToken(String token, String secretKey) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                .parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
