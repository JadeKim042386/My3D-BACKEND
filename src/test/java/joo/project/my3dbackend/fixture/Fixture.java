package joo.project.my3dbackend.fixture;

import joo.project.my3dbackend.domain.*;
import joo.project.my3dbackend.domain.constants.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class Fixture {

    public static UserAccount createUserAccount(
            String email, String password, String nickname, String phone, Address address, UserRole userRole) {
        UserAccount userAccount = UserAccount.ofGeneralUser(email, password, nickname, phone, address, userRole);
        ReflectionTestUtils.setField(userAccount, "id", 1L);
        return userAccount;
    }

    public static UserAccount createCompanyUserAccount(
            String email, String password, String nickname, String phone, Address address, UserRole userRole, Company company) {
        UserAccount userAccount = UserAccount.ofCompanyUser(email, password, nickname, phone, address, userRole, company);
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

    public static UserAccount createCompanyUserAccount() {
        return createCompanyUserAccount(
                "testUser@gmail.com",
                "pw",
                "testUser",
                "01012341234",
                Address.of("12345", "street", "detail"),
                UserRole.USER,
                createCompany());
    }

    public static Article createArticle(
            String title,
            String content,
            ArticleType articleType,
            ArticleCategory articleCategory,
            boolean isFree,
            Long userAccountId) {
        return Article.ofModel(
                title, content, articleType, articleCategory, isFree, userAccountId, createArticleFile());
    }

    public static Article createArticle() {
        Article article = createArticle("title", "content", ArticleType.MODEL, ArticleCategory.MUSIC, true, 1L);
        ReflectionTestUtils.setField(article, "userAccount", Fixture.createUserAccount());
        return article;
    }

    public static ArticleComment createArticleComment(Long parentCommentId) {
        ArticleComment articleComment = ArticleComment.of("content", 1L, 1L, parentCommentId);
        ReflectionTestUtils.setField(articleComment, "userAccount", Fixture.createUserAccount());
        ReflectionTestUtils.setField(articleComment, "id", 1L);
        ReflectionTestUtils.setField(articleComment, "createdAt", LocalDateTime.now());
        return articleComment;
    }

    public static ArticleFile createArticleFile() {
        return ArticleFile.of(100L, "test.stl", "test", "stl", createDimensionOption());
    }

    public static Dimension createDimension() {
        return new Dimension("dimName", 11.11, DimUnit.MM);
    }

    public static DimensionOption createDimensionOption() {
        DimensionOption dimensionOption = DimensionOption.of("optionName");
        dimensionOption.getDimensions().add(createDimension());
        return dimensionOption;
    }

    public static MockMultipartFile createMultipartFile(String content) {
        String fileName = "test.stl";
        return new MockMultipartFile("modelFile", fileName, "text/plain", content.getBytes(StandardCharsets.UTF_8));
    }

    public static MockMultipartFile createMultipartFile() {
        return createMultipartFile("It's test content.");
    }

    public static Company createCompany() {
        Company company = Company.of("test", "test.com");
        ReflectionTestUtils.setField(company, "id", 1L);
        return company;
    }

    public static Alarm createAlarm() {
        Alarm alarm = Alarm.of(AlarmType.NEW_COMMENT, 1L, 1L, 1L, 2L);
        ReflectionTestUtils.setField(alarm, "sender", createUserAccount());
        ReflectionTestUtils.setField(alarm, "article", createArticle());
        return alarm;
    }
}
