package dev.challduck.portfolio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {

    // 사용자 로그인페이지 요청
    @GetMapping("/login")
    public String login(){return "login";}

    // 사용자 회원가입 페이지 요청
    @GetMapping("/signup")
    private String signup () {return "signup";}

    @GetMapping("/upload")
    public String imgUpload(){return "upload";}
}
