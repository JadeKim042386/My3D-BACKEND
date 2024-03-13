package joo.project.my3dbackend.utils;

import joo.project.my3dbackend.exception.FileException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileUtils {

    private static final int NOT_FOUND = -1;
    private static final String EXTENSION_SEPARATOR = ".";
    private static Set<String> MODEL_EXTENSIONS = Set.of("stl", "stp");

    /**
     * 확장자 index 반환
     */
    public static int indexOfExtension(String filename) {
        if (!StringUtils.hasText(filename)) {
            throw new FileException(ErrorCode.INVALID_FIlE_NAME);
        }
        return filename.lastIndexOf(EXTENSION_SEPARATOR);
    }

    /**
     * 확장자만 추출 (e.g. stl)
     */
    public static String getExtension(String filename) {
        int index = indexOfExtension(filename);
        return filename.substring(index + 1);
    }

    /**
     * 임의의 고유한 파일명 생성
     */
    public static String generateUniqueFileName(String extension) {
        if (!isCompatibleExtension(extension)) {
            throw new FileException(ErrorCode.INVALID_FIlE_NAME);
        }
        return UUID.randomUUID() + EXTENSION_SEPARATOR + extension;
    }

    /**
     * 비어있는 파일인지 호환가능한 파일 형식인지 판단 (비어있을 경우 true 반환)
     */
    public static boolean isValid(MultipartFile file) {
        return !isEmpty(file) && isCompatibleExtension(file);
    }

    private static boolean isCompatibleExtension(MultipartFile file) {
        String extension = getExtension(file.getOriginalFilename());
        return MODEL_EXTENSIONS.contains(extension);
    }

    private static boolean isCompatibleExtension(String extension) {
        return MODEL_EXTENSIONS.contains(extension);
    }

    /**
     * null이거나 파일명이 비어있거나 10bytes 이하라면 비어있다고 판단합니다.
     * 10bytes로 정한 이유는 javascript에서 파일을 선택하지 않을 경우 기본적으로 'undefined' 내용을 포함하는
     * 파일을 임의로 생성하게됩니다. 그리고 이때 파일의 크기가 10bytes입니다. 모델 파일 크기는 가장 간단한 형상이라도 10bytes를 가볍게 뛰어넘습니다.
     */
    private static boolean isEmpty(MultipartFile file) {
        return Objects.isNull(file) || file.getSize() <= 10 || !StringUtils.hasText(file.getOriginalFilename());
    }
}
