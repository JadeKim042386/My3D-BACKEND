package joo.project.my3dbackend.fixture;

import joo.project.my3dbackend.domain.ArticleComment;
import joo.project.my3dbackend.domain.constants.UserRole;
import joo.project.my3dbackend.dto.ArticleCommentDto;
import joo.project.my3dbackend.dto.ArticleWithCommentDto;
import joo.project.my3dbackend.dto.request.ArticleCommentRequest;
import joo.project.my3dbackend.dto.request.ArticleRequest;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public class FixtureDto {
    private static final String DEFAULT_CONTENT = "content";

    public static ArticleRequest createArticleRequest(
            String title, String content, String articleCategory, Boolean isFree) {
        ArticleRequest articleRequest = new ArticleRequest();
        articleRequest.setTitle(title);
        articleRequest.setContent(content);
        articleRequest.setArticleCategory(articleCategory);
        articleRequest.setIsFree(isFree);
        return articleRequest;
    }

    public static ArticleRequest createArticleRequest() {
        return createArticleRequest("title", DEFAULT_CONTENT, "MUSIC", true);
    }

    public static UserPrincipal createUserPrincipal(
            Long id, String email, String password, UserRole userRole, String nickname) {
        return new UserPrincipal(id, email, password, Set.of(new SimpleGrantedAuthority(userRole.getName())), nickname);
    }

    public static UserPrincipal createUserPrincipal() {
        return createUserPrincipal(1L, "testUser@gmail.com", "pw", UserRole.USER, "testUser");
    }

    public static ArticleWithCommentDto createArticleWithCommentDto() {
        return ArticleWithCommentDto.fromEntity(Fixture.createArticleWithComment());
    }

    public static ArticleCommentRequest createArticleCommentRequest(String content, Long parentCommentId) {
        return new ArticleCommentRequest(content, Optional.ofNullable(parentCommentId));
    }

    public static ArticleCommentRequest createArticleCommentRequest() {
        return createArticleCommentRequest(DEFAULT_CONTENT, null);
    }

    public static ArticleCommentDto createArticleCommentDto(ArticleComment articleComment) {
        return ArticleCommentDto.fromEntity(articleComment);
    }

    public static ArticleCommentDto createArticleCommentDto() {
        return createArticleCommentDto(Fixture.createArticleComment(null));
    }

    public static ArticleCommentDto createArticleChildCommentDto(Long parentCommentId) {
        return createArticleCommentDto(Fixture.createArticleComment(parentCommentId));
    }
}
