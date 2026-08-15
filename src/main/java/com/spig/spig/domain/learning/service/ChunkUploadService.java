package com.spig.spig.domain.learning.service;

import com.spig.spig.domain.learning.dto.ChunkUploadInitRequestDto;
import com.spig.spig.domain.learning.dto.ChunkUploadInitResponseDto;
import com.spig.spig.domain.learning.dto.ChunkUploadRequestDto;
import com.spig.spig.domain.learning.dto.ChunkUploadResponseDto;

public interface ChunkUploadService {

    public ChunkUploadInitResponseDto createSession(ChunkUploadInitRequestDto request);

}
