package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.exception.FileException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import joo.project.my3dbackend.repository.ArticleFileRepository;
import joo.project.my3dbackend.service.ArticleFileServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleFileService implements ArticleFileServiceInterface {
    private final ArticleFileRepository articleFileRepository;

    @Override
    public String findUploadedFileName(Long articleId) {
        return articleFileRepository
                .findFileNameByArticleId(articleId)
                .orElseThrow(() -> new FileException(ErrorCode.FILE_NOT_FOUND));
    }
}
