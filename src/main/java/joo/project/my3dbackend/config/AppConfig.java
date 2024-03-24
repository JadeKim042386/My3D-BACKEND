package joo.project.my3dbackend.config;

import joo.project.my3dbackend.service.FileServiceInterface;
import joo.project.my3dbackend.service.impl.LocalFileService;
import joo.project.my3dbackend.service.impl.S3Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.Arrays;

@Configuration
public class AppConfig {

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
    }

    @Bean
    public FileServiceInterface fileService(Environment env, S3Service s3Service, LocalFileService localFileService) {
        String activatedProfile =
                Arrays.stream(env.getActiveProfiles()).findFirst().orElse("local");
        if (activatedProfile.startsWith("prod")) {
            return s3Service;
        } else {
            return localFileService;
        }
    }
}
