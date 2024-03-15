package joo.project.my3dbackend.service;

public interface ArticleFileServiceInterface {

    /**
     * 게시글에 업로드된 파일명 조회
     */
    String findUploadedFileName(Long articleId);
}
