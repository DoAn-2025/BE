package com.doan2025.webtoeic.repository;

import com.doan2025.webtoeic.domain.Post;
import com.doan2025.webtoeic.dto.SearchBaseDto;
import com.doan2025.webtoeic.dto.response.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = """
        SELECT new com.doan2025.webtoeic.dto.response.PostResponse(
            p.id, p.title, p.content, p.themeUrl, p.createdAt,
             p.updatedAt, p.isActive, p.isDelete, p.categoryPost)
        FROM Post p
        LEFT JOIN p.author u
        WHERE (COALESCE(:#{#dto.title}, null ) is null or p.title LIKE CONCAT('%',:#{#dto.title},'%'))
            AND ( (COALESCE(:#{#dto.fromDate}, null ) is null AND COALESCE(:#{#dto.toDate}, null ) is null )
                    OR p.createdAt between :#{#dto.fromDate} and :#{#dto.toDate})
            AND (COALESCE(:#{#dto.categoryPost}, null) is null or  p.categoryPost in (:#{#dto.categoryPost}) )
            
""")
    Page<PostResponse> findPostByManagerWithFilter(@Param("dto") SearchBaseDto dto, @Param("pageable") Pageable pageable);

    @Query(value = """
        SELECT new com.doan2025.webtoeic.dto.response.PostResponse(
            p.id, p.title, p.content, p.themeUrl, p.createdAt,
             p.updatedAt, p.isActive, p.isDelete,p.categoryPost)
        FROM Post p
        WHERE p.isDelete = FALSE
            AND p.author.email = :#{#dto.email}
            AND (COALESCE(:#{#dto.title}, null ) is null or p.title LIKE CONCAT('%',:#{#dto.title},'%'))
             AND ( (COALESCE(:#{#dto.fromDate}, null ) is null AND COALESCE(:#{#dto.toDate}, null ) is null )
                    OR p.createdAt between :#{#dto.fromDate} and :#{#dto.toDate})
            AND (COALESCE(:#{#dto.categoryPost}, null) is null or p.categoryPost in (:#{#dto.categoryPost}))
            
""")
    Page<PostResponse> findOwnPostsWithFilter(@Param("dto") SearchBaseDto dto, Pageable pageable);

    @Query(value = """
            SELECT new com.doan2025.webtoeic.dto.response.PostResponse(p.id, p.title, p.content, p.themeUrl, p.categoryPost, p.createdAt)
            FROM Post p
            WHERE p.isDelete = FALSE AND p.isActive = TRUE
                AND (COALESCE(:#{#dto.title}, null ) is null or p.title LIKE CONCAT('%',:#{#dto.title},'%'))
                 AND ( (COALESCE(:#{#dto.fromDate}, null ) is null AND COALESCE(:#{#dto.toDate}, null ) is null )
                    OR p.createdAt between :#{#dto.fromDate} and :#{#dto.toDate})
                AND (COALESCE(:#{#dto.categoryPost}, null) is null or p.categoryPost in (:#{#dto.categoryPost}) )
""")
    Page<PostResponse> findPostFilter(@Param("dto") SearchBaseDto dto, Pageable pageable);

    @Query(value = """
        SELECT new com.doan2025.webtoeic.dto.response.PostResponse(
            p.id, p.title, p.content, p.themeUrl, p.createdAt,
            p.updatedAt, p.isActive, p.isDelete, p.categoryPost,
            CASE
                WHEN p.author.email = :email THEN TRUE
                ELSE FALSE
            END as isOwn )
        FROM Post p
        WHERE p.id = :id AND p.isDelete = FALSE AND p.isActive = TRUE
""")
    Optional<PostResponse> findPostDetail(Long id, String email);
}
