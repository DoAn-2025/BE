package com.doan2025.webtoeic.utils;

import com.doan2025.webtoeic.constants.Constants;
import com.doan2025.webtoeic.constants.enums.EGender;
import com.doan2025.webtoeic.constants.enums.ERole;
import com.doan2025.webtoeic.domain.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class CommonUtil {

    public static String generatedUserCode(ERole role) {
        return switch (role) {
            case TEACHER -> Constants.PRE_CODE_TEACHER + new Random().nextLong(100_000_000, 999_999_999);
            case CONSULTANT -> Constants.PRE_CODE_CONSULTANT + new Random().nextLong(100_000_000, 999_999_999);
            case MANAGER -> Constants.PRE_CODE_MANAGER + new Random().nextLong(100_000_000, 999_999_999);
            default -> Constants.PRE_CODE_STUDENT + new Random().nextLong(100_000_000, 999_999_999);
        };
    }

    public static String replaceValueResetPassword(User user, Integer otp) {
        return Constants.BODY_REST_PASSWORD
                .replace(Constants.USERNAME, user.getFirstName() + " " + user.getLastName())
                .replace(Constants.OTP_CODE, String.valueOf(otp));
    }

    public static Integer otpGenerator() {
        Random r = new Random();
        return r.nextInt(100_000, 999_999);
    }

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
