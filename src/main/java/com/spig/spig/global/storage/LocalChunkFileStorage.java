package com.spig.spig.global.storage;

import com.spig.spig.domain.learning.dto.ChunkUploadRequestDto;
import com.spig.spig.domain.learning.entity.ChunkUpload;
import com.spig.spig.global.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.spig.spig.global.exception.ErrorCode.*;

@Component
public class LocalChunkFileStorage implements ChunkFileStorage {

    private final Path tempDirectory;
    private final Path finalFileDirectory;

    public LocalChunkFileStorage(
            @Value("${file.upload.temp-directory}")
            String tempDirectory,

            @Value("${file.upload.directory}")
            String finalFileDirectory
    ) {
        this.tempDirectory = Paths.get(
                tempDirectory
        ).toAbsolutePath().normalize();

        this.finalFileDirectory = Paths.get(
                finalFileDirectory
        ).toAbsolutePath().normalize();
    }

    @Override
    public void saveChunk(ChunkUploadRequestDto request) {
        try {
            Path uploadDirectory = tempDirectory
                    .resolve(request.getUploadId().toString())
                    .normalize();

            if (!uploadDirectory.startsWith(tempDirectory)) {
                throw new IllegalArgumentException(
                        "잘못된 업로드 경로입니다."
                );
            }

            Files.createDirectories(uploadDirectory);

            Path chunkPath = uploadDirectory.resolve(
                    "chunk-" + request.getChunkNumber() + ".part"
            );

            request.getChunk().transferTo(chunkPath);
        } catch (IOException exception) {
            throw new CustomException(CHUNK_STORAGE_ERROR);
        }
    }

    @Override
    public void mergeChunks(ChunkUpload upload) {
        Path uploadDirectory = tempDirectory
                .resolve(upload.getUploadId().toString())
                .normalize();

        if (!uploadDirectory.startsWith(tempDirectory)) {
            throw new IllegalArgumentException(
                    "잘못된 업로드 경로입니다."
            );
        }

        String extension =
                StringUtils.getFilenameExtension(
                        upload.getOriginalName()
                );

        String storedName =
                upload.getUploadId()+"."+extension;

        Path mergingPath = finalFileDirectory.resolve(
                storedName + ".part"
        );

        Path finalPath = finalFileDirectory.resolve(
                storedName
        );

        try {
            Files.createDirectories(finalFileDirectory);

            try (OutputStream outputStream =
                         Files.newOutputStream(
                                 finalPath,
                                 StandardOpenOption.CREATE,
                                 StandardOpenOption.TRUNCATE_EXISTING
                         )) {

                for (
                        int chunkNumber = 0;
                        chunkNumber < upload.getTotalChunks();
                        chunkNumber++
                ) {
                    Path chunkPath = uploadDirectory.resolve(
                            "chunk-" + chunkNumber + ".part"
                    );

                    validateChunkFile(
                            upload,
                            chunkNumber,
                            chunkPath
                    );

                    Files.copy(
                            chunkPath,
                            outputStream
                    );
                }
            }

            if (Files.size(finalPath)
                    != upload.getTotalSize()) {
                throw new IllegalStateException(
                        "병합된 파일 크기가 원본 크기와 다릅니다."
                );
            }

            upload.success();

//            return Files.move(
//                    mergingPath,
//                    finalPath,
//                    StandardCopyOption.REPLACE_EXISTING
//            );
        } catch (IOException exception) {
            throw new CustomException(CHUNK_MERGE_FAIL);
        }
    }

    @Override
    public void deleteChunks(UUID uploadId) {
        Path uploadDirectory = tempDirectory
                .resolve(uploadId.toString())
                .normalize();

        if (!uploadDirectory.startsWith(tempDirectory)) {
            throw new IllegalArgumentException(
                    "잘못된 업로드 경로입니다."
            );
        }

        if (!Files.exists(uploadDirectory)) {
            return;
        }

        try (Stream<Path> paths =
                     Files.walk(uploadDirectory)) {

            List<Path> deleteTargets = paths
                    .sorted(Comparator.reverseOrder())
                    .toList();

            for (Path target : deleteTargets) {
                Files.deleteIfExists(target);
            }
        } catch (IOException exception) {
            throw new CustomException(CHUNK_FOLDER_DELETE_ERROR);
        }
    }


    private void validateChunkFile(
            ChunkUpload upload,
            int chunkNumber,
            Path chunkPath
    ) throws IOException {
        if (!Files.isRegularFile(chunkPath)) {
            throw new IllegalStateException(
                    "청크 파일이 누락되었습니다: "
                            + chunkNumber
            );
        }

        long expectedSize;

        boolean lastChunk =
                chunkNumber
                        == upload.getTotalChunks() - 1;

        if (lastChunk) {
            expectedSize =
                    upload.getTotalSize()
                            - upload.getChunkSize()
                            * (upload.getTotalChunks() - 1L);
        } else {
            expectedSize = upload.getChunkSize();
        }

        if (Files.size(chunkPath) != expectedSize) {
            throw new IllegalStateException(
                    "청크 크기가 올바르지 않습니다: "
                            + chunkNumber
            );
        }
    }
}
