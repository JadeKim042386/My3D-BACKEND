package joo.project.my3dbackend.fixture;

import joo.project.my3dbackend.domain.ArticleComment;
import joo.project.my3dbackend.domain.constants.*;
import joo.project.my3dbackend.dto.ArticleCommentDto;
import joo.project.my3dbackend.dto.ArticleDto;
import joo.project.my3dbackend.dto.CompanyDto;
import joo.project.my3dbackend.dto.request.*;
import joo.project.my3dbackend.dto.security.UserPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Optional;
import java.util.Set;

public class FixtureDto {
    private static final String DEFAULT_CONTENT = "content";

    public static ArticleRequest createArticleRequest(
            String title, String content, ArticleType articleType, ArticleCategory articleCategory, Boolean isFree) {
        ArticleRequest articleRequest = new ArticleRequest();
        articleRequest.setTitle(title);
        articleRequest.setContent(content);
        articleRequest.setArticleType(articleType);
        articleRequest.setArticleCategory(articleCategory);
        articleRequest.setIsFree(isFree);
        articleRequest.setDimensionOption(createDimensionOptionRequest());
        return articleRequest;
    }

    public static ArticleRequest createArticleRequest() {
        return createArticleRequest("title", DEFAULT_CONTENT, ArticleType.MODEL, ArticleCategory.MUSIC, true);
    }

    public static DimensionOptionRequest createDimensionOptionRequest() {
        DimensionOptionRequest dimensionOptionRequest = new DimensionOptionRequest();
        dimensionOptionRequest.setOptionName("testOption");
        dimensionOptionRequest.getDimensions().add(createDimensionRequest());
        return dimensionOptionRequest;
    }

    public static DimensionRequest createDimensionRequest() {
        DimensionRequest dimensionRequest = new DimensionRequest();
        dimensionRequest.setName("testName");
        dimensionRequest.setValue(11.1);
        dimensionRequest.setUnit(DimUnit.MM);
        return dimensionRequest;
    }

    public static UserPrincipal createUserPrincipal(
            Long id, String email, String password, UserRole userRole, String nickname) {
        return new UserPrincipal(
                id,
                email,
                password,
                Set.of(new SimpleGrantedAuthority(userRole.getName())),
                nickname,
                SubscribeStatus.SUBSCRIBE);
    }

    public static UserPrincipal createUserPrincipal() {
        return createUserPrincipal(1L, "testUser@gmail.com", "pw", UserRole.USER, "testUser");
    }

    public static ArticleDto createArticleDto() {
        return ArticleDto.fromEntity(Fixture.createArticle());
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

    public static SignUpRequest createSignUpRequest(
            String email,
            UserRole userRole,
            String nickname,
            String password,
            String phone,
            String zipcode,
            String street,
            String detail,
            String companyName) {
        return new SignUpRequest(email, userRole, nickname, password, phone, zipcode, street, detail, companyName);
    }

    public static SignUpRequest createSignUpRequest() {
        return createSignUpRequest(
                "test@gmail.com",
                UserRole.USER,
                "test",
                "test1234@@",
                "01011112222",
                "12345",
                "street",
                "detail",
                null);
    }

    public static SignUpRequest createCompanySignUpRequest() {
        return createSignUpRequest(
                "testCompany@gmail.com",
                UserRole.COMPANY,
                "testCompany",
                "test1234@@",
                "01011112222",
                "12345",
                "street",
                "detail",
                "Fake");
    }

    public static AdminRequest createAdminRequest(
            String nickname, String phone, String zipcode, String street, String detail) {
        return new AdminRequest(nickname, phone, zipcode, street, detail);
    }

    public static AdminRequest createAdminRequest() {
        return createAdminRequest("test", "01011112222", "12345", "street", "detail");
    }

    public static PasswordRequest createPasswordRequest() {
        return new PasswordRequest("test2345@@");
    }

    public static CompanyRequest createCompanyRequest() {
        return new CompanyRequest("my3d", "my3d.com");
    }

    public static CompanyDto createCompanyDto(String companyName, String homepage) {
        return new CompanyDto(1L, companyName, homepage);
    }
}
