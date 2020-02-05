package com.example.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
public class HelloController {

    @Value("${security.oauth2.client.access-token-uri}")
    private String accessTokenUri;

    @RequestMapping("/hello")
    public String hello(){
        return "hello world";
    }

    @RequestMapping("/hello2")
    public String hello2(){
        return  "hello 瓜田李下";
    }

    @RequestMapping("/redirect")
    public Map get(@RequestParam(value = "code") String code){
        OkHttpClient httpClient=new OkHttpClient();

        RequestBody requestBody=new FormBody.Builder()
                .add("grant_type","authorization_code")
                .add("client","user")
                .add("redirect_uri","http://localhost:8082/redirect")
                .add("code",code)
                .build();

        Request request=new Request.Builder()
                .url(accessTokenUri)
                .post(requestBody)
                .addHeader("Authorization","Basic dXNlcjoxMjM0NTY=")
                .build();

        Map result=null;

        try {
            Response response=httpClient.newCall(request).execute();
            System.out.println(response);

            ObjectMapper objectMapper=new ObjectMapper();
            result=objectMapper.readValue(Objects.requireNonNull(response.body()).string(),Map.class);

            System.out.println("access_token："+result.get("access_token"));
            System.out.println("token_type："+result.get("token_type"));
            System.out.println("refresh_token："+result.get("refresh_token"));
            System.out.println("expires_in："+result.get("expires_in"));
            System.out.println("scope："+result.get("scope"));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return result;
    }
}
