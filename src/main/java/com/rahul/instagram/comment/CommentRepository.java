package com.rahul.instagram.comment;

import com.rahul.instagram.post.Post;
import com.rahul.instagram.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostOrderByCreatedAtDesc(Post post);
    void deleteByPostAndUser(Post post, User user);
}
