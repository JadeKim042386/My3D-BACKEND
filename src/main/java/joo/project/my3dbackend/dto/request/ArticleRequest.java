package joo.project.my3dbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import joo.project.my3dbackend.domain.Article;
import joo.project.my3dbackend.domain.ArticleFile;
import joo.project.my3dbackend.domain.DimensionOption;
import joo.project.my3dbackend.domain.constants.ArticleCategory;
import joo.project.my3dbackend.domain.constants.ArticleType;
import joo.project.my3dbackend.exception.ArticleException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import joo.project.my3dbackend.utils.FileUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
@Setter
@NoArgsConstructor
public class ArticleRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    @JsonFormat(shape = STRING)
    private ArticleType articleType;

    @JsonFormat(shape = STRING) // for objectMapper
    private ArticleCategory articleCategory;

    @NotNull
    private Boolean isFree;

    @Valid
    private DimensionOptionRequest dimensionOption;

    public Article toEntity(Long userAccountId, MultipartFile modelFile) {
        // ArticleType.MODEL은 파일, 치수 정보, 카테고리를 가져야하지만, ArticleType.TEXT는 가지지 못합니다.
        switch (articleType) {
            case MODEL:
                return toModelEntity(userAccountId, modelFile);
            default:
                return toTextEntity(userAccountId);
        }
    }

    private Article toModelEntity(Long userAccountId, MultipartFile modelFile) {
        return Article.ofModel(
                title,
                content,
                articleType,
                getArticleCategory(),
                isFree,
                userAccountId,
                toArticleFileEntity(modelFile));
    }

    private Article toTextEntity(Long userAccountId) {
        return Article.ofText(title, content, ArticleType.TEXT, isFree, userAccountId);
    }

    /**
     * MultipartFile -> ArticleFile
     */
    private ArticleFile toArticleFileEntity(MultipartFile modelFile) {
        // 파일이 비어있거나 호환되지 않는 경우
        if (!FileUtils.isValid(modelFile)) {
            throw new ArticleException(ErrorCode.INVALID_FIlE);
        }
        String originalFileName = modelFile.getOriginalFilename();
        String extension = FileUtils.getExtension(originalFileName);
        return ArticleFile.of(
                modelFile.getSize(),
                originalFileName,
                FileUtils.generateUniqueFileName(extension),
                extension,
                getDimensionOptionEntity());
    }

    private ArticleCategory getArticleCategory() {
        return Optional.ofNullable(articleCategory).orElseThrow(() -> new ArticleException(ErrorCode.INVALID_CATEGORY));
    }

    private DimensionOption getDimensionOptionEntity() {
        return Optional.ofNullable(dimensionOption)
                .map(DimensionOptionRequest::toEntity)
                .orElseThrow(() -> new ArticleException(ErrorCode.INVALID_DIMENSION));
    }
}
