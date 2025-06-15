package com.doan2025.webtoeic.service;

import com.doan2025.webtoeic.dto.request.FileRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface CloudService {
    String uploadFile(MultipartFile file) throws IOException;
    Map deleteFile(FileRequest dto) throws IOException;
}
