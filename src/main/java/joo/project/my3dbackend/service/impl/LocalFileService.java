package joo.project.my3dbackend.service.impl;

import joo.project.my3dbackend.dto.properties.LocalFileProperties;
import joo.project.my3dbackend.exception.FileException;
import joo.project.my3dbackend.exception.constants.ErrorCode;
import joo.project.my3dbackend.service.FileServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class LocalFileService implements FileServiceInterface {
    private final LocalFileProperties localFileProperties;

    @Override
    public void uploadFile(MultipartFile file, String fileName) {
        try (FileOutputStream fos = new FileOutputStream(createFile(fileName))) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            throw new FileException(ErrorCode.FILE_CANT_SAVE);
        }
    }

    @Override
    public void deleteFile(String fileName) {
        File file = createFile(fileName);
        if (file.exists()) {
            if (!file.delete()) {
                throw new FileException(ErrorCode.FILE_CANT_DELETE);
            }
        } else {
            throw new FileException(ErrorCode.FILE_NOT_FOUND);
        }
    }

    private File createFile(String fileName) {
        return new File(localFileProperties.absolutePath() + fileName);
    }
}
