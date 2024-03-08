package joo.project.my3dbackend.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileUtils {

    private static final int NOT_FOUND = -1;
    private static final String EXTENSION_SEPARATOR = ".";

    /**
     * 확장자 index 반환
     */
    public static int indexOfExtension(String filename) {
        if (filename == null) {
            return NOT_FOUND;
        }
        return filename.lastIndexOf(EXTENSION_SEPARATOR);
    }

    /**
     * 확장자만 추출 (e.g. stl)
     */
    public static String getExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int index = indexOfExtension(filename);
        if (index == NOT_FOUND) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }

    /**
     * 임의의 고유한 파일명 생성
     */
    public static String generateUniqueFileName(String extension) {
        return UUID.randomUUID() + "." + extension;
    }
}
