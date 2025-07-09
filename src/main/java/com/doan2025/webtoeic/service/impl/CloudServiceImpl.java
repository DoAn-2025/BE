package com.doan2025.webtoeic.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.doan2025.webtoeic.constants.Constants;
import com.doan2025.webtoeic.constants.enums.ResponseCode;
import com.doan2025.webtoeic.constants.enums.ResponseObject;
import com.doan2025.webtoeic.dto.request.FileRequest;
import com.doan2025.webtoeic.exception.WebToeicException;
import com.doan2025.webtoeic.service.CloudService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = { WebToeicException.class, Exception.class })
public class CloudServiceImpl implements CloudService {
    private final Cloudinary cloudinary;

    /**
     * Upload tệp lên Cloudinary và tự động xác định loại tệp
     * param file Tệp được gửi từ client
     * @return URL của tệp đã upload
     */
    public String uploadFile(MultipartFile file) {
        try {
            // Lấy tên tệp và phần mở rộng
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new WebToeicException(ResponseCode.IS_NULL, ResponseObject.FILE);
            }
            String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

            // Xác định loại tệp từ phần mở rộng
            String fileType = Constants.EXTENSION_TO_TYPE.get(extension);
            if (fileType == null) {
                throw new WebToeicException(ResponseCode.UNSUPPORTED, ResponseObject.FILE);
            }

            // Xác định thư mục lưu trữ trên Cloudinary
            String folder = fileType + "s"; // images, videos, pdfs, docs

            // Cấu hình upload
            Map<String, Object> uploadOptions = new HashMap<>();
            uploadOptions.put(Constants.FOLDER, folder);

            // Cấu hình tối ưu hóa theo loại tệp
            if (fileType.equals(Constants.IMAGE) || fileType.equals(Constants.VIDEO)) {
                uploadOptions.put(Constants.QUALITY, Constants.AUTO);
                uploadOptions.put(Constants.FETCH_FORMAT, Constants.AUTO);
            }
            uploadOptions.put(Constants.RESOURCE_TYPE, fileType.equals(Constants.IMAGE) ? Constants.IMAGE :
                    fileType.equals(Constants.VIDEO) ? Constants.VIDEO : Constants.DOCUMENT);

            // Upload tệp lên Cloudinary
            Map data = this.cloudinary.uploader().upload(file.getBytes(), uploadOptions);

            // Trả về URL của tệp
            return (String) data.get(Constants.URL);
        } catch (IOException io) {
            throw new WebToeicException(ResponseCode.CANNOT_UPLOAD, ResponseObject.FILE);
        }
    }

    /**
     * Xóa tệp trên Cloudinary dựa trên URL
     * param url của tệp trên Cloudinary
     * param fileType Loại tệp: image, video, pdf, doc
     * @return Map chứa kết quả xóa
     */
    public Map deleteFile(FileRequest dto) {
        try {
            // Kiểm tra loại tệp hợp lệ
            if (!Constants.ALLOWED_EXTENSIONS.containsKey(dto.getFileType())) {
                throw new WebToeicException(ResponseCode.INVALID, ResponseObject.FILE);
            }

            // Trích xuất public_id từ URL
            String publicId = extractPublicId(dto.getUrl());
            // Xác định resource_type
            String resourceType = dto.getFileType().equals(Constants.IMAGE) ? Constants.IMAGE :
                    dto.getFileType().equals(Constants.VIDEO) ? Constants.VIDEO : Constants.DOCUMENT;
            // Xóa tệp theo public_id
            Map result = this.cloudinary.uploader().destroy(publicId,
                    ObjectUtils.asMap(Constants.RESOURCE_TYPE, resourceType));
            return result;
        } catch (IOException e) {
            throw new WebToeicException(ResponseCode.CANNOT_DELETE, ResponseObject.FILE);
        }
    }

    public Double getVideoDuration(String url) {
        try{
            String publicId = extractPublicId(url);
            publicId = publicId.substring(0, publicId.lastIndexOf("."));
            Map res = cloudinary.api().resource(publicId, ObjectUtils.asMap(
                    "resource_type", "video","media_metadata", true
            ));
            return (Double) res.get(Constants.DURATION);
        }catch (Exception e){
            throw new WebToeicException(ResponseCode.UNSUPPORTED, ResponseObject.FILE);
        }
    }

    /**
     * Trích xuất public_id từ URL của tệp trên Cloudinary
     * @param fileUrl URL của tệp
     * @return public_id của tệp
     */
    private String extractPublicId(String fileUrl) {
        fileUrl = fileUrl.replace("upload/", "upload/q_auto,f_auto/");
        String[] urlParts = fileUrl.split(Constants.REGEX_FILE_TYPE);
        if (urlParts.length > 1) {
            return urlParts[1].replaceAll(Constants.REGEX_FILE, "");
        }
        throw new WebToeicException(ResponseCode.INVALID, ResponseObject.URL);
    }
}
