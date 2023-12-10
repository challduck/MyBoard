package dev.challduck.portfolio.service;

import dev.challduck.portfolio.domain.Article;
import dev.challduck.portfolio.domain.ArticleImage;
import dev.challduck.portfolio.dto.article.ArticleImageUploadRequest;
import dev.challduck.portfolio.repository.ArticleImageRepository;
import dev.challduck.portfolio.util.ImageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class ArticleImageService {
    private final ArticleImageRepository articleImageRepository;
//    private final ArticleImage articleImage;


    public void saveImage(List<MultipartFile> files, Article article) throws Exception {

        String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());

        String path = "img/article_resource/" + currentDate;
        File directory = new File(path);

        if(!directory.exists()){ directory.mkdirs(); }

        for (MultipartFile file : files) {
            UUID fileName = UUID.randomUUID();

            if(!file.isEmpty()){
                ImageType imageType = ImageType.findByContentType(file.getContentType());

                if (imageType == null){
                    break;
                }

                // 프로젝트 절대경로
                String projectAbsolutePath = new File("").getAbsolutePath() + "\\";
                String newFileName = fileName + imageType.getFormat();
                ArticleImage articleImage = new ArticleImageUploadRequest().toEntity(article, fileName.toString() ,imageType.getFormat());

                File targetFile = new File(projectAbsolutePath + path + File.separator + newFileName);
                log.info("targetFile : {}", targetFile);
                articleImageRepository.save(articleImage);

                // 이미지 최적화 및 저장
                Thumbnails.of(file.getInputStream())
                        .size(640, 480)
                        .outputFormat(imageType.getFormat().substring(1))
                        .toFile(targetFile);

                // spring boot MultipartFile 객체로 image save
                // file.transferTo(targetFile);
            }
        }

//
//        String fileFormat = getFileFormat(file);
//        log.info("fileFormat : {}", fileFormat);
//
//
//        // 프로젝트 절대경로 2
//        String absolutePath = System.getProperty("user.dir");
//        log.info("absolutePath : {}",absolutePath);
//
//        String filePath = projectAbsolutePath + "src/main/resources/static/img/article_source/" + fileName + "." + fileFormat;
//
//        log.info("filePath : {}", filePath);
//        // 이미지 저장
//
//        File destFile = new File(filePath);
//        log.info("destFile : {}", destFile);
//
//        file.transferTo(destFile);
////            Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
//
//        ArticleImage request = new ArticleImageUploadRequest().toEntity(article, fileName.toString(), fileFormat);
//
//        articleImageRepository.save(request);
    }


}
