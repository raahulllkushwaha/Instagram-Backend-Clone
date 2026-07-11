package com.rahul.instagram.post;

import com.rahul.instagram.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
   List<Post> findByUserOrderByCreatedAtDesc(User user);
}
