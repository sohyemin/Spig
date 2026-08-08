package com.spig.spig.domain.learning.service;

import com.spig.spig.domain.learning.dto.UploadResponseDto;
import com.spig.spig.domain.learning.entity.LearningFile;
import com.spig.spig.domain.learning.repository.LearningFileRepository;
import com.spig.spig.global.storage.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LearningFileUploadServiceImpl
        implements LearningFileUploadService {

    private final LearningFileRepository learningFileRepository;
    private final FileStorage fileStorage;

    @Override
    @Transactional
    public UploadResponseDto upload(MultipartFile file) {
        validateFile(file);

        Path storedPath = fileStorage.save(file);

        try {
            LearningFile learningFile =
                    createLearningFile(file, storedPath);

            learningFile.markSuccess();

            LearningFile savedFile =
                    learningFileRepository.save(learningFile);

            return UploadResponseDto.to(savedFile);

        } catch (RuntimeException exception) {
            fileStorage.delete(storedPath.toString());
            throw exception;
        }
    }

    @Override
    public UploadResponseDto findById(Long fileId) {
        return UploadResponseDto.to(getLearningFile(fileId));
    }

    @Override
    public List<UploadResponseDto> findAll() {
        return learningFileRepository.findAll()
                .stream()
                .map(UploadResponseDto::to)
                .toList();
    }

    @Override
    @Transactional
    public UploadResponseDto update(
            Long fileId,
            MultipartFile file
    ) {
        validateFile(file);

        LearningFile learningFile =
                getLearningFile(fileId);

        String previousStoragePath =
                learningFile.getStoragePath();

        Path newStoredPath = fileStorage.save(file);

        try {
            learningFile.replace(
                    getOriginalName(file),
                    newStoredPath.getFileName().toString(),
                    newStoredPath.toString(),
                    file.getContentType(),
                    file.getSize()
            );

            learningFile.markSuccess();

            fileStorage.delete(previousStoragePath);

            return UploadResponseDto.to(learningFile);

        } catch (RuntimeException exception) {
            fileStorage.delete(newStoredPath.toString());
            throw exception;
        }
    }

    @Override
    @Transactional
    public void delete(Long fileId) {
        LearningFile learningFile =
                getLearningFile(fileId);

        fileStorage.delete(
                learningFile.getStoragePath()
        );

        learningFileRepository.delete(learningFile);
    }

    private LearningFile getLearningFile(Long fileId) {
        return learningFileRepository.findById(fileId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "존재하지 않는 학습 파일입니다. id="
                                        + fileId
                        )
                );
    }

    private LearningFile createLearningFile(
            MultipartFile file,
            Path storedPath
    ) {
        return LearningFile.from(
                getOriginalName(file),
                storedPath.getFileName().toString(),
                storedPath.toString(),
                file.getContentType(),
                file.getSize()
        );
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "업로드할 파일이 비어 있습니다."
            );
        }

        if (file.getOriginalFilename() == null
                || file.getOriginalFilename().isBlank()) {
            throw new IllegalArgumentException(
                    "파일 이름이 존재하지 않습니다."
            );
        }
    }

    private String getOriginalName(MultipartFile file) {
        return Path.of(file.getOriginalFilename())
                .getFileName()
                .toString();
    }
}