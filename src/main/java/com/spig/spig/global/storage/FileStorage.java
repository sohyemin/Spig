package com.spig.spig.global.storage;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileStorage {

    Path save(MultipartFile file);

    void delete(String storagePath);
}
