package com.rahul.instagram.story;

import com.rahul.instagram.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {
    List<Story> findByUserOrderByCreatedAtDesc(User user);

    List<Story> findByExpiresAtBefore(LocalDateTime now);

    List<Story> findByUser_IdInOrderByCreatedAtDesc(List<Long> userIds);
}
