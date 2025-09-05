package com.doan2025.webtoeic.service;

import com.doan2025.webtoeic.dto.request.ClassRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface ClassMemberService {
    void addUserToClass(HttpServletRequest request, ClassRequest classRequest);

    void removeUserFromClass(HttpServletRequest request, ClassRequest classRequest);
}
