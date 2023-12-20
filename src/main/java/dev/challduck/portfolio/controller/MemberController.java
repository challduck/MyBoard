package dev.challduck.portfolio.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MemberController {

    // 사용자 로그인페이지 요청
    @GetMapping("/login")
    public String login(){return "login";}

    // 사용자 회원가입 페이지 요청
    @GetMapping("/signup")
    private String signup () {return "signup";}

    @GetMapping("/article-form")
    public String articleForm(){
        return "articleForm";
    }

    @GetMapping("/upload")
    public String imgUpload(){return "upload";}

    @GetMapping("/")
    public String home(){return "index";}

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id){
        if(id == null){
            return "errorPage";
        }
        return "articleView";
    }
    @GetMapping("/new-article")
    public String newArticle(){return "articleForm";}
    @GetMapping("/new-article/{id}")
    public String editArticle(@PathVariable Long id){return "articleForm";}
    @GetMapping("/my-page")
    public String myPage(){return "myPage";}
    @GetMapping("/error")
    public String errorPage(){return "errorPage";}
}
