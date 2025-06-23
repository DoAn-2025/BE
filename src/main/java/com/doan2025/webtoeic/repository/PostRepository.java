package com.doan2025.webtoeic.repository;

import com.doan2025.webtoeic.domain.Post;
import com.doan2025.webtoeic.dto.SearchBaseDto;
import com.doan2025.webtoeic.dto.response.PostResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = """
        SELECT new com.doan2025.webtoeic.dto.response.PostResponse(
            p.id, p.title, p.content, p.themeUrl, p.createdAt,
             p.updatedAt, p.isActive, p.isDelete)
        FROM Post p
        WHERE (COALESCE(:#{#dto.title}, null ) is null or p.title LIKE CONCAT('%',:#{#dto.title},'%'))
            AND p.createdAt between :#{#dto.fromDate} and :#{#dto.toDate}
            AND (COALESCE(:#{dto.categoryPost}, null) is null or p.categoryPost in (:#{dto.categoryPost}) )
            
""")
    Optional<List<PostResponse>> findPostByManagerWithFilter(SearchBaseDto dto, Pageable pageable);

    @Query(value = """
        SELECT new com.doan2025.webtoeic.dto.response.PostResponse(
            p.id, p.title, p.content, p.themeUrl, p.createdAt,
             p.updatedAt, p.isActive, p.isDelete)
        FROM Post p
        WHERE p.isDelete = FALSE AND p.isActive = TRUE
            AND p.author.email = :#{#dto.email}
            AND (COALESCE(:#{#dto.title}, null ) is null or p.title LIKE CONCAT('%',:#{#dto.title},'%'))
            AND p.createdAt between :#{#dto.fromDate} and :#{#dto.toDate}
            AND (COALESCE(:#{dto.categoryPost}, null) is null or p.categoryPost in (:#{dto.categoryPost}))
            
""")
    Optional<List<PostResponse>> findOwnPostsWithFilter(SearchBaseDto dto, Pageable pageable);

    @Query(value = """
            SELECT new com.doan2025.webtoeic.dto.response.PostResponse(p.id, p.title, p.content, p.themeUrl, p.createdAt)
            FROM Post p
            WHERE p.isDelete = FALSE AND p.isActive = TRUE
                AND (COALESCE(:#{#dto.title}, null ) is null or p.title LIKE CONCAT('%',:#{#dto.title},'%'))
                AND p.createdAt between :#{#dto.fromDate} and :#{#dto.toDate} 
                AND (COALESCE(:#{dto.categoryPost}, null) is null or p.categoryPost in (:#{dto.categoryPost}) )
""")
    Optional<List<PostResponse>> findPostFilter(SearchBaseDto dto, Pageable pageable);

    @Query(value = """
        SELECT new com.doan2025.webtoeic.dto.response.PostResponse(
            p.id, p.title, p.content, p.themeUrl, p.createdAt,
            p.updatedAt, p.isActive, p.isDelete,
            CASE
                WHEN p.author.email = :email THEN TRUE
                ELSE FALSE
            END as isAuthor )
        FROM Post p
        WHERE p.id = :id AND p.isDelete = FALSE AND p.isActive = TRUE
""")
    Optional<PostResponse> findPostDetail(Long id, String email);
}
