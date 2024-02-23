package joo.project.my3dbackend.fixture;

import joo.project.my3dbackend.dto.request.ArticleRequest;

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
}
