package joo.project.my3dbackend.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 로컬(개발) 환경과 운영 환경을 구분하여 구현합니다.
 */
public interface FileServiceInterface {
    void uploadFile(MultipartFile file, String fileName);

    void deleteFileIfExists(String fileName);

    byte[] downloadFile(String fileName);
}
