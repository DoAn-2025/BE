package com.doan2025.webtoeic.utils;

import com.doan2025.webtoeic.constants.enums.EGender;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {

    // Hàm parse dob từ String sang Date
    public static Date parseDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false); // Không chấp nhận định dạng lỏng lẻo
            return dateFormat.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Định dạng ngày không hợp lệ, yêu cầu định dạng yyyy-MM-dd: " + date);
        }
    }

    // Hàm chuyển đổi Integer sang EGender
    public static EGender convertIntegerToEGender(Integer num) {
        if (num == null) return null;
        return switch (num) {
            case 1 -> EGender.MALE;
            case 2 -> EGender.FEMALE;
            case 3 -> EGender.OTHER;
            default -> throw new IllegalArgumentException("Giá trị giới tính không hợp lệ: " + num);
        };
    }
}
