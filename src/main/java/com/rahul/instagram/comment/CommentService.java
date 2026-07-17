package com.rahul.instagram.comment;

import com.rahul.instagram.comment.dto.CommentResponse;
import com.rahul.instagram.comment.dto.CreateCommentRequest;
import com.rahul.instagram.common.exceptions.ResourceNotFoundException;
import com.rahul.instagram.notification.NotificationEvent;
import com.rahul.instagram.notification.NotificationProducer;
import com.rahul.instagram.notification.NotificationType;
import com.rahul.instagram.post.Post;
import com.rahul.instagram.post.PostRepository;
import com.rahul.instagram.user.User;
import com.rahul.instagram.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final CommentMapper commentMapper;

    //new for kafka
    private final NotificationProducer notificationProducer;


    @Transactional(transactionManager = "transactionManager")
    public CommentResponse addComment(String currentUsername, Long postId, CreateCommentRequest request){

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + currentUsername));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with this post ID: " + postId));



                Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(request.getContent())
                .build();

        Comment savedComment = commentRepository.save(comment);

        if (!post.getUser().getId().equals(user.getId())) {
            NotificationEvent event = new NotificationEvent(
                    post.getUser().getId(),
                    user.getUsername(),
                    NotificationType.COMMENT,
                    user.getUsername() + " commented on your post"
            );
            notificationProducer.sendNotification(event);
        }

        return commentMapper.toCommentResponse(savedComment);
    }

    public List<CommentResponse> getCommentsByPost(Long postId){

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with this post ID: " + postId));

        List<Comment> comments = commentRepository.findByPostOrderByCreatedAtDesc(post);

        return comments.stream()
                .map(commentMapper::toCommentResponse)
                .toList();
    }

    @Transactional(transactionManager = "transactionManager")
    public void deleteComment(String currentUsername, Long commentId){

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getUser().getUsername().equals(currentUsername)) {
            throw new IllegalArgumentException("You are not authorized to delete this comment");
        }

        commentRepository.deleteById(commentId);
    }


}
