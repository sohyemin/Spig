package com.spig.spig.global.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@Component
public class LocalFileStorage implements FileStorage {

    private final Path uploadDirectory;

    public LocalFileStorage(
            @Value("${file.upload.directory}")
            String uploadDirectory
    ) {
        this.uploadDirectory = Path.of(uploadDirectory)
                .toAbsolutePath()
                .normalize();

        createUploadDirectory();
    }

    @Override
    public Path save(MultipartFile file) {
        String storedName = createStoredName(
                file.getOriginalFilename()
        );

        Path targetPath = uploadDirectory
                .resolve(storedName)
                .normalize();

        try {
            file.transferTo(targetPath);

            log.info(
                    "파일 저장 완료: originalName={}, storedName={}",
                    file.getOriginalFilename(),
                    storedName
            );

            return targetPath;

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "파일 저장에 실패했습니다.",
                    exception
            );
        }
    }

    @Override
    public void delete(String storagePath) {
        Path targetPath = Path.of(storagePath)
                .toAbsolutePath()
                .normalize();

        validateStoragePath(targetPath);

        try {
            Files.deleteIfExists(targetPath);
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "파일 삭제에 실패했습니다.",
                    exception
            );
        }
    }

    private void createUploadDirectory() {
        try {
            Files.createDirectories(uploadDirectory);
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "파일 저장 폴더를 생성할 수 없습니다.",
                    exception
            );
        }
    }

    private String createStoredName(String originalName) {
        String cleanName = StringUtils.cleanPath(
                originalName == null ? "" : originalName
        );

        String extension =
                StringUtils.getFilenameExtension(cleanName);

        if (extension == null || extension.isBlank()) {
            return UUID.randomUUID().toString();
        }

        return UUID.randomUUID()
                + "."
                + extension.toLowerCase(Locale.ROOT);
    }

    private void validateStoragePath(Path targetPath) {
        if (!targetPath.startsWith(uploadDirectory)) {
            throw new IllegalArgumentException(
                    "허용되지 않은 파일 경로입니다."
            );
        }
    }
}