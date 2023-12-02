package dev.challduck.portfolio.controller;

import dev.challduck.portfolio.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ArticleApiController {
    private final ArticleService articleService;

}
