package joo.project.my3dbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import joo.project.my3dbackend.domain.Article;
import joo.project.my3dbackend.domain.ArticleFile;
import joo.project.my3dbackend.domain.DimensionOption;
import joo.project.my3dbackend.domain.constants.ArticleCategory;
import joo.project.my3dbackend.domain.constants.ArticleType;
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

    @JsonFormat(shape = STRING) // for objectMapper
    private ArticleCategory articleCategory;

    @NotNull
    private Boolean isFree;

    @Valid
    private DimensionOptionRequest dimensionOption;

    public Article toEntity(Long userAccountId, MultipartFile modelFile) {
        // ArticleType.MODEL은 파일, 치수 정보, 카테고리를 가져야하지만, ArticleType.TEXT는 가지지 못합니다.
        return Optional.ofNullable(modelFile)
                .map(multipartFile -> Article.ofModel(
                        title,
                        content,
                        ArticleType.MODEL,
                        getArticleCategory(),
                        isFree,
                        userAccountId,
                        toArticleFileEntity(multipartFile)))
                .orElseGet(() -> Article.ofText(title, content, ArticleType.TEXT, isFree, userAccountId));
    }

    public ArticleFile toArticleFileEntity(MultipartFile modelFile) {
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
        return Optional.ofNullable(articleCategory)
                .orElseThrow(() -> new NullPointerException("articleCategory is Null"));
    }

    private DimensionOption getDimensionOptionEntity() {
        return Optional.ofNullable(dimensionOption)
                .map(DimensionOptionRequest::toEntity)
                .orElseThrow(() -> new NullPointerException("dimensionOption is Null"));
    }
}
