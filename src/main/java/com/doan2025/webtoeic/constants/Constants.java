package com.doan2025.webtoeic.constants;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Constants {
    public static final String USERNAME = "username";
    public static final String OTP_CODE = "OTP_Code";
    public static final String SUBJECT_RESET_PASSWORD = "[Hệ thống TOEIC] Mã xác thực đặt lại mật khẩu của bạn";
    public static final String BODY_REST_PASSWORD = "Xin chào username,\n" +
            "\n" +
            "Bạn đã yêu cầu đặt lại mật khẩu cho tài khoản của mình. Vui lòng sử dụng mã OTP bên dưới để hoàn tất quá trình đặt lại mật khẩu.\n" +
            "\n" +
            "\uD83D\uDD12 Mã OTP của bạn là: OTP_Code\n" +
            "⏰ Mã có hiệu lực trong vòng 5 phút kể từ thời điểm gửi.\n" +
            "\n" +
            "Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này hoặc liên hệ với chúng tôi để được hỗ trợ.\n" +
            "\n" +
            "Trân trọng,\n" +
            "Hệ thống luyện thi TOEIC";

    public static final String REGEX_FILE_TYPE = "/v[0-9]+/";
    public static final String REGEX_FILE = "\\.[a-zA-Z]{3,4}$";
    public static final String QUALITY = "quality";
    public static final String FETCH_FORMAT = "fetch_format";
    public static final String AUTO = "auto";
    public static final String RESOURCE_TYPE = "resource_type";

    public static final String URL = "url";
    public static final String VIDEO = "video";
    public static final String IMAGE = "image";
    public static final String DOCUMENT = "document";

    public static final String FOLDER = "folder";

    public static final String JPG = "jpg";
    public static final String PNG = "png";
    public static final String GIF = "gif";
    public static final String BMP = "bmp";
    public static final String JPEG = "jpeg";

    public static final String MP4 = "mp4";
    public static final String MOV = "mov";
    public static final String AVI = "avi";
    public static final String MKV = "mkv";

    public static final String PDF = "pdf";

    public static final String DOC = "doc";
    public static final String DOCX = "docx";
    public static final String XLS = "xls";
    public static final String XLSX = "xlsx";
    public static final String PPT = "ppt";
    public static final String PPTX = "pptx";

    public static final List<String> IMAGE_TYPES = Arrays.asList(JPG, PNG, GIF, BMP, JPEG);
    public static final List<String> VIDEO_TYPES = Arrays.asList(MP4, MOV, AVI, MKV);
    public static final List<String> DOCUMENT_TYPES = Arrays.asList(PDF, DOCX, DOC, XLS, XLSX, PPT, PPTX);

    public static final List<String> UPLOAD_TYPES = new ArrayList<>();
    public static final Map<String, List<String>> ALLOWED_EXTENSIONS = new HashMap<>();
    public static final Map<String, String> EXTENSION_TO_TYPE = new HashMap<>();

    static {
        UPLOAD_TYPES.addAll(IMAGE_TYPES);
        UPLOAD_TYPES.addAll(VIDEO_TYPES);
        UPLOAD_TYPES.addAll(DOCUMENT_TYPES);

        ALLOWED_EXTENSIONS.put(VIDEO, VIDEO_TYPES);
        ALLOWED_EXTENSIONS.put(IMAGE, IMAGE_TYPES);
        ALLOWED_EXTENSIONS.put(DOCUMENT, DOCUMENT_TYPES);

        EXTENSION_TO_TYPE.put(JPG, IMAGE);
        EXTENSION_TO_TYPE.put(JPEG, IMAGE);
        EXTENSION_TO_TYPE.put(PNG, IMAGE);
        EXTENSION_TO_TYPE.put(GIF, IMAGE);
        EXTENSION_TO_TYPE.put(BMP, IMAGE);
        EXTENSION_TO_TYPE.put(MP4, VIDEO);
        EXTENSION_TO_TYPE.put(MOV, VIDEO);
        EXTENSION_TO_TYPE.put(AVI, VIDEO);
        EXTENSION_TO_TYPE.put(MKV, VIDEO);
        EXTENSION_TO_TYPE.put(PDF, DOCUMENT);
        EXTENSION_TO_TYPE.put(DOC, DOCUMENT);
        EXTENSION_TO_TYPE.put(DOCX, DOCUMENT);
        EXTENSION_TO_TYPE.put(XLSX, DOCUMENT);
        EXTENSION_TO_TYPE.put(XLS, DOCUMENT);
        EXTENSION_TO_TYPE.put(PPT, DOCUMENT);
        EXTENSION_TO_TYPE.put(PPTX, DOCUMENT);
    }


}
