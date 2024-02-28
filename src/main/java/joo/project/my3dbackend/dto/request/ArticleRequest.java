package joo.project.my3dbackend.dto.request;

import joo.project.my3dbackend.domain.Article;
import joo.project.my3dbackend.domain.UserAccount;
import joo.project.my3dbackend.domain.constants.ArticleCategory;
import joo.project.my3dbackend.domain.constants.ArticleType;
import joo.project.my3dbackend.dto.request.validation.ValidEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class ArticleRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    @ValidEnum(enumClass = ArticleCategory.class)
    private String articleCategory;

    @NotNull
    private Boolean isFree;

    // TODO: modelFile(dimension)
    // TODO: userAccount

    public Article toEntity(Long userAccountId) {
        // TODO: model 파일이 있을 경우 ArticleType.MODEL, 없으면 TEXT
        return Article.of(title, content, ArticleType.TEXT, ArticleCategory.valueOf(articleCategory), isFree, userAccountId);
    }
}
