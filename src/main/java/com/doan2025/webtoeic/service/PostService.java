package com.doan2025.webtoeic.service;

import com.doan2025.webtoeic.dto.SearchBaseDto;
import com.doan2025.webtoeic.dto.request.PostRequest;
import com.doan2025.webtoeic.dto.response.PostResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.internal.bytebuddy.agent.builder.AgentBuilder;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    List<PostResponse> getAllPosts(SearchBaseDto dto, Pageable pageable);
    List<PostResponse> getOwnPosts(HttpServletRequest request, SearchBaseDto dto, Pageable pageable);
    List<PostResponse> getPosts(SearchBaseDto dto, Pageable pageable);
    PostResponse getPostDetail(HttpServletRequest request, Long id); // get detail
    PostResponse createPost(PostRequest postRequest);
    PostResponse updatePost(HttpServletRequest request, PostRequest postRequest);
    PostResponse disableOrDeletePost(HttpServletRequest request, PostRequest postRequest);
}
