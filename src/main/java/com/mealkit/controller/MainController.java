package com.mealkit.controller;

import com.mealkit.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@Slf4j
@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String root(){
        return "index";
    }

    @GetMapping("/signup")
    public String signup(){
        return "signUp";
    }

    //여기서부터 아이디 관련 Controller 새로 만들것.
    @GetMapping("/findUser")
    public String find(){
        return "Find/findUser";
    }

    @GetMapping("/findPw")
        public String findPw(){
            return "Find/findPw";
    }

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public String check(
            @RequestParam(value = "accessToken")String accessToken, ModelMap map) throws JSONException {
      //  ModelMap map = new ModelMap();

        String[] parts = accessToken.split("\\.");
        JSONObject header = new JSONObject(decode(parts[0]));
        JSONObject payload = new JSONObject(decode(parts[1]));
        String signature = decode(parts[2]);
        log.info("check" + header + payload + signature);
        String userName = payload.getString("sub");
        String userId= payload.getString("id");
        System.out.println(userName);
        if(userRepository.existsByUserName(userName)) {
            map.addAttribute("userName", userName);
            map.addAttribute("userId", userId);
        }

        return "/header :: #loginStatus";
    }
    private static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }
}


