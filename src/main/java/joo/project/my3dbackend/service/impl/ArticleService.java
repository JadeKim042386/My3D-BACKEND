package joo.project.my3dbackend.service.impl;

import com.querydsl.core.types.Predicate;
import joo.project.my3dbackend.domain.Article;
import joo.project.my3dbackend.domain.constants.ArticleType;
import joo.project.my3dbackend.dto.ArticleDto;
import joo.project.my3dbackend.dto.request.ArticleRequest;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import joo.project.my3dbackend.exception.ArticleException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import joo.project.my3dbackend.repository.ArticleRepository;
import joo.project.my3dbackend.service.ArticleServiceInterface;
import joo.project.my3dbackend.service.FileServiceInterface;
import joo.project.my3dbackend.utils.FileUtils;
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
    public Page<ArticleDto> getArticles(Predicate predicate, Pageable pageable) {
        return articleRepository.findAll(predicate, pageable).map(ArticleDto::fromEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public ArticleDto getArticleDtoWithFile(Long articleId) {
        return ArticleDto.fromEntity(getArticleWithFile(articleId));
    }

    @Transactional(readOnly = true)
    @Override
    public Article getArticleWithFile(Long articleId) {
        return articleRepository
                .findFetchAllById(articleId)
                .orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_ARTICLE));
    }

    @Override
    public ArticleDto writeArticle(
            MultipartFile modelFile, ArticleRequest articleRequest, UserPrincipal userPrincipal) {

        checkValidModelFile(articleRequest.getArticleType(), modelFile);
        Article savedArticle = articleRepository.save(articleRequest.toEntity(userPrincipal.id(), modelFile));
        uploadModelFile(modelFile, savedArticle);
        return ArticleDto.fromEntity(savedArticle, userPrincipal);
    }

    @Override
    public ArticleDto updateArticle(
            MultipartFile modelFile, ArticleRequest articleRequest, Long articleId, UserPrincipal userPrincipal) {
        Article savedArticle = getArticleWithFile(articleId);
        // 기본적으로 제목, 본문을 수정할 수 있다
        if (isUpdatedArticleData(savedArticle.getTitle(), articleRequest.getTitle())) {
            savedArticle.setTitle(articleRequest.getTitle());
        }
        if (isUpdatedArticleData(savedArticle.getContent(), articleRequest.getContent())) {
            savedArticle.setContent(articleRequest.getContent());
        }
        // MODEL 게시글은 추가로 치수, 파일, 카테고리를 수정할 수 있다
        if (articleRequest.getArticleType() == ArticleType.MODEL) {
            // 카테고리는 수정된 경우에만 수정
            if (isUpdatedArticleData(savedArticle.getArticleCategory(), articleRequest.getArticleCategory())) {
                savedArticle.setArticleCategory(articleRequest.getArticleCategory());
            }
            // 파일이 유효할 경우 치수 정보를 포함한 파일 전체를 대체, 유효하지 않을 경우 치수만 대체
            if (FileUtils.isValid(modelFile)) {
                savedArticle.setArticleFile(articleRequest.toArticleFileEntity(modelFile));
            } else {
                savedArticle.getArticleFile().setDimensionOption(articleRequest.getDimensionOptionEntity());
            }
        }
        return ArticleDto.fromEntity(savedArticle, userPrincipal);
    }

    @Override
    public void deleteArticle(Long articleId) {
        // TODO: 게시글 삭제 쿼리 확인
        articleRepository.deleteById(articleId);
    }

    @Transactional(readOnly = true)
    @Override
    public Long getUserAccountIdOfArticle(Long articleId) {
        return articleRepository
                .findUserAccountIdById(articleId)
                .orElseThrow(() -> new ArticleException(ErrorCode.NOT_FOUND_ARTICLE));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isWriterOfArticle(Long userAccountId, Long articleId) {
        return articleRepository.existsByIdAndUserAccount_Id(articleId, userAccountId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isFreeArticle(Long articleId) {
        return articleRepository.getArticleIsFreeStatus(articleId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsArticle(Long articleId) {
        return articleRepository.existsById(articleId);
    }

    /**
     * 파일이 존재할 경우 업로드(저장)
     */
    private void uploadModelFile(MultipartFile file, Article savedArticle) {
        Optional.ofNullable(savedArticle.getArticleFile())
                .ifPresent(articleFile -> fileService.uploadFile(file, articleFile.getFileName()));
    }

    /**
     * MODEL 타입의 게시글일 경우 파일이 유효한지 검사
     */
    private void checkValidModelFile(ArticleType articleType, MultipartFile modelFile) {
        if (articleType == ArticleType.MODEL && !FileUtils.isValid(modelFile))
            throw new ArticleException(ErrorCode.INVALID_FIlE);
    }

    /**
     * 데이터가 수정되었는지 확인
     */
    private <T> boolean isUpdatedArticleData(T savedData, T updatedData) {
        return !savedData.equals(updatedData);
    }
}
