package joo.project.my3dbackend.dto;

import joo.project.my3dbackend.domain.ArticleFile;

public record ArticleFileDto(Long id, Long byteSize, String originalFileName, String fileName, String fileExtension) {

    public static ArticleFileDto fromEntity(ArticleFile articleFile) {
        return new ArticleFileDto(
                articleFile.getId(),
                articleFile.getByteSize(),
                articleFile.getOriginalFileName(),
                articleFile.getFileName(),
                articleFile.getFileExtension());
    }
}
