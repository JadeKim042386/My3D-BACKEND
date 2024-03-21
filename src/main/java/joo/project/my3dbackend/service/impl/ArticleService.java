package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.domain.Article;
import joo.project.my3dbackend.dto.ArticleDto;
import joo.project.my3dbackend.dto.request.ArticleRequest;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import joo.project.my3dbackend.exception.ArticleException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import joo.project.my3dbackend.repository.ArticleRepository;
import joo.project.my3dbackend.service.ArticleServiceInterface;
import joo.project.my3dbackend.service.FileServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService implements ArticleServiceInterface {
    private final ArticleRepository articleRepository;
    private final FileServiceInterface fileService;

    @Transactional(readOnly = true)
    @Override
    public Page<ArticleDto> getArticles(Pageable pageable) {
        return articleRepository.findAll(pageable).map(ArticleDto::fromEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public ArticleDto getArticle(Long articleId) {
        return articleRepository
                .findFetchAllById(articleId)
                .map(ArticleDto::fromEntity)
                .orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_ARTICLE));
    }

    @Override
    public ArticleDto writeArticle(
            MultipartFile modelFile, ArticleRequest articleRequest, UserPrincipal userPrincipal) {

        Article savedArticle = articleRepository.save(articleRequest.toEntity(userPrincipal.id(), modelFile));
        uploadModelFile(modelFile, savedArticle);
        return ArticleDto.fromEntity(savedArticle, userPrincipal);
    }

    @Override
    public void deleteArticle(Long articleId) {
        // TODO: 게시글이 존재할 경우에 삭제할 수 있다.
        articleRepository.deleteById(articleId);
    }

    @Override
    public Long getUserAccountIdOfArticle(Long articleId) {
        return articleRepository.findUserAccountIdById(articleId)
                .orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_ARTICLE));
    }

    @Override
    public boolean isWriterOfArticle(Long userAccountId, Long articleId) {
        return articleRepository.existsByIdAndUserAccount_Id(articleId, userAccountId);
    }

    /**
     * 파일이 존재할 경우 업로드(저장)
     */
    private void uploadModelFile(MultipartFile file, Article savedArticle) {
        Optional.ofNullable(savedArticle.getArticleFile())
                .ifPresent(articleFile -> fileService.uploadFile(file, articleFile.getFileName()));
    }
}
