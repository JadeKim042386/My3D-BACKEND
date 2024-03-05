package joo.project.my3dbackend.fixture;

import joo.project.my3dbackend.domain.Address;
import joo.project.my3dbackend.domain.Article;
import joo.project.my3dbackend.domain.ArticleComment;
import joo.project.my3dbackend.domain.UserAccount;
import joo.project.my3dbackend.domain.constants.ArticleCategory;
import joo.project.my3dbackend.domain.constants.ArticleType;
import joo.project.my3dbackend.domain.constants.UserRole;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

public class Fixture {

    public static UserAccount createUserAccount(
            String email, String password, String nickname, String phone, Address address, UserRole userRole) {
        UserAccount userAccount = UserAccount.of(email, password, nickname, phone, address, userRole);
        ReflectionTestUtils.setField(userAccount, "id", 1L);
        return userAccount;
    }

    public static UserAccount createUserAccount() {
        return createUserAccount(
                "testUser@gmail.com",
                "pw",
                "testUser",
                "01012341234",
                Address.of("12345", "street", "detail"),
                UserRole.USER);
    }

    public static Article createArticle(
            String title,
            String content,
            ArticleType articleType,
            ArticleCategory articleCategory,
            boolean isFree,
            Long userAccountId) {
        return Article.of(title, content, articleType, articleCategory, isFree, userAccountId);
    }

    public static Article createArticleWithComment() {
        Article article = createArticle("title", "content", ArticleType.MODEL, ArticleCategory.MUSIC, true, 1L);
        article.getArticleComments().add(createArticleComment());
        ReflectionTestUtils.setField(article, "userAccount", Fixture.createUserAccount());
        return article;
    }

    public static ArticleComment createArticleComment() {
        ArticleComment articleComment = ArticleComment.of("content", 1L, 1L, null);
        ReflectionTestUtils.setField(articleComment, "userAccount", Fixture.createUserAccount());
        ReflectionTestUtils.setField(articleComment, "id", 1L);
        ReflectionTestUtils.setField(articleComment, "createdAt", LocalDateTime.now());
        return articleComment;
    }
}
