package com.rahul.instagram.like;

import com.rahul.instagram.post.Post;
import com.rahul.instagram.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByPostAndUser(Post post, User user);
    long countByPost(Post post); // total post like
    void deleteByPostAndUser(Post post, User user); //to unlike

}
