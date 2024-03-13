package joo.project.my3dbackend.dto.response;

public record ArticleLikeResponse(int likeCount) {
    public static ArticleLikeResponse of(int likeCount) {
        return new ArticleLikeResponse(likeCount);
    }
}
