package com.rahul.instagram.like;

import com.rahul.instagram.common.exceptions.ResourceNotFoundException;
import com.rahul.instagram.notification.NotificationEvent;
import com.rahul.instagram.notification.NotificationProducer;
import com.rahul.instagram.notification.NotificationType;
import com.rahul.instagram.post.Post;
import com.rahul.instagram.post.PostRepository;
import com.rahul.instagram.user.User;
import com.rahul.instagram.user.UserRepository;
import com.rahul.instagram.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // for notifications
    private final NotificationProducer notificationProducer;

    @Transactional(transactionManager = "transactionManager")
    public void likePost(String currentUsername, Long postId){

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + currentUsername));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with this post ID: " + postId));

        if(likeRepository.existsByPostAndUser(post, user)){
            throw new RuntimeException("User: " + user + "already like this " + post);
        }

        Like like = Like.builder()
                .post(post)
                .user(user)
                .build();

        likeRepository.save(like);

        // new code to send notifications
        if(!post.getUser().getId().equals(user.getId())){
            NotificationEvent event = new NotificationEvent(
                    post.getUser().getId(), // recipientId - owner of the post
                    user.getUsername(), //sendUsername - whom who like the post
                    NotificationType.LIKE,
                    user.getUsername() + "liked your post"
            );
            notificationProducer.sendNotification(event);
        }
    }

    @Transactional(transactionManager = "transactionManager")
    public void unlikePost(String currentUsername, Long postId) {

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + currentUsername));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with this post ID: " + postId));


        if (!likeRepository.existsByPostAndUser(post, user)) {
            throw new RuntimeException("You have not liked this post");
        }
        likeRepository.deleteByPostAndUser(post, user);
    }

    public long getLikeCount(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with this post ID: " + postId));

        return likeRepository.countByPost(post);
    }

    public boolean isLikedByUser(String currentUsername, Long postId){
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + currentUsername));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with this post ID: " + postId));

        return likeRepository.existsByPostAndUser(post, user);
    }
}
