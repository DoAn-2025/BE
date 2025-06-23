package com.doan2025.webtoeic.service.impl;

import com.doan2025.webtoeic.constants.enums.ECategoryPost;
import com.doan2025.webtoeic.constants.enums.ResponseCode;
import com.doan2025.webtoeic.constants.enums.ResponseObject;
import com.doan2025.webtoeic.domain.Post;
import com.doan2025.webtoeic.domain.User;
import com.doan2025.webtoeic.dto.SearchBaseDto;
import com.doan2025.webtoeic.dto.request.PostRequest;
import com.doan2025.webtoeic.dto.response.PostResponse;
import com.doan2025.webtoeic.exception.WebToeicException;
import com.doan2025.webtoeic.repository.PostRepository;
import com.doan2025.webtoeic.repository.UserRepository;
import com.doan2025.webtoeic.service.PostService;
import com.doan2025.webtoeic.service.UserService;
import com.doan2025.webtoeic.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;


    @Override
    public List<PostResponse> getAllPosts(SearchBaseDto dto, Pageable pageable) {
        return postRepository.findPostByManagerWithFilter(dto,pageable)
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.POST));
    }

    @Override
    public List<PostResponse> getOwnPosts(HttpServletRequest request, SearchBaseDto dto, Pageable pageable) {
        String email = jwtUtil.getEmailFromToken(request);
        dto.setEmail(email);
        return postRepository.findOwnPostsWithFilter(dto,pageable)
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.POST));
    }

    @Override
    public List<PostResponse> getPosts(SearchBaseDto dto, Pageable pageable) {
        return postRepository.findPostFilter(dto,pageable)
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.POST));
    }

    @Override
    public PostResponse getPostDetail(HttpServletRequest request, Long id) {
        String email = jwtUtil.getEmailFromToken(request);

        return postRepository.findPostDetail(id, email)
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.POST) );
    }


    @Override
    public PostResponse createPost(HttpServletRequest request, PostRequest postRequest) {
        Post post = modelMapper.map(postRequest, Post.class);
        String email = jwtUtil.getEmailFromToken(request);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.USER));
        post.setAuthor(user);

        ECategoryPost categoryPost = ECategoryPost.fromValue(postRequest.getCategoryId());
        post.setCategoryPost(categoryPost);

        return modelMapper.map(postRepository.save(post), PostResponse.class);
    }

    @Override
    public PostResponse updatePost(HttpServletRequest request, PostRequest postRequest) {
        return null;
    }

    @Override
    public PostResponse disableOrDeletePost(HttpServletRequest request, PostRequest postRequest) {
        return null;
    }
}
