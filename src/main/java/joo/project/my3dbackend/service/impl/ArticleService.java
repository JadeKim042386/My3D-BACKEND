package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.domain.Article;
import joo.project.my3dbackend.dto.ArticleDto;
import joo.project.my3dbackend.dto.ArticleWithCommentDto;
import joo.project.my3dbackend.dto.request.ArticleRequest;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import joo.project.my3dbackend.exception.ArticleException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import joo.project.my3dbackend.repository.ArticleRepository;
import joo.project.my3dbackend.service.ArticleServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService implements ArticleServiceInterface {
    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    @Override
    public ArticleWithCommentDto getArticleWithComment(Long articleId) {
        return articleRepository
                .findFetchAllById(articleId)
                .map(ArticleWithCommentDto::fromEntity)
                .orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_ARTICLE));
    }

    @Override
    public ArticleDto writeArticle(ArticleRequest articleRequest, UserPrincipal userPrincipal) {
        Article savedArticle = articleRepository.save(articleRequest.toEntity(userPrincipal.id()));
        return ArticleDto.fromEntity(savedArticle, userPrincipal);
    }

    @Override
    public void deleteArticle(Long articleId) {
        articleRepository.deleteById(articleId);
    }
}
