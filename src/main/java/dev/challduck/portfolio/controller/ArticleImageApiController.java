package dev.challduck.portfolio.controller;

import dev.challduck.portfolio.domain.Article;
import dev.challduck.portfolio.domain.ArticleImage;
import dev.challduck.portfolio.dto.article.ArticleImageUploadResponse;
import dev.challduck.portfolio.service.ArticleImageService;
import dev.challduck.portfolio.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ArticleImageApiController {

    private final ArticleImageService articleImageService;
    private final ArticleService articleService;

    // article image 조회 메서드
    @GetMapping("/images/{id}")
    public ResponseEntity getImage(
            @PathVariable Long id
    ){

        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    // article image 저장 메서드
    @PostMapping("/images/{id}")
    public ResponseEntity<?> saveImage(@PathVariable Long id, @RequestParam("files") List<MultipartFile> file){
        try{
            articleImageService.saveImage(file, articleService.findById(id));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("저장 실패");
        }
        return ResponseEntity.status(HttpStatus.OK).body("저장 성공");

    }
}
