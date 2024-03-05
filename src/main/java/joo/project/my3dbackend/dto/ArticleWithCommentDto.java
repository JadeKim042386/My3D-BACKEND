package joo.project.my3dbackend.dto;

import joo.project.my3dbackend.domain.Article;
import joo.project.my3dbackend.domain.ArticleComment;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public record ArticleWithCommentDto(ArticleDto article, Set<ArticleCommentDto> articleComments) {
    public static ArticleWithCommentDto fromEntity(Article article) {
        return new ArticleWithCommentDto(
                ArticleDto.fromEntity(article), organizeChildComments(article.getArticleComments()));
    }

    private static Set<ArticleCommentDto> organizeChildComments(Set<ArticleComment> comments) {
        return comments.stream()
                // 부모 댓글만 filtering
                .filter(ArticleComment::isParentComment)
                // 부모 댓글 DTO에 자식 댓글 DTO 추가
                .map(comment -> {
                    ArticleCommentDto parentComment = ArticleCommentDto.fromEntity(comment);
                    parentComment
                            .childComments()
                            .addAll(comment.getChildComments().stream()
                                    .map(ArticleCommentDto::fromEntity)
                                    .collect(Collectors.toUnmodifiableSet()));
                    return parentComment;
                })
                // 생성일시(내림차순), id 기준 정렬
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ArticleCommentDto::createdAt)
                        .reversed()
                        .thenComparingLong(ArticleCommentDto::id))));
    }
}
