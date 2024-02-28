package joo.project.my3dbackend.fixture;

import joo.project.my3dbackend.domain.constants.UserRole;
import joo.project.my3dbackend.dto.ArticleWithCommentDto;
import joo.project.my3dbackend.dto.request.ArticleRequest;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

public class FixtureDto {

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
        return createArticleRequest("title", "content", "MUSIC", true);
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
}
