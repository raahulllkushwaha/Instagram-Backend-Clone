package com.rahul.instagram.follow;

import com.rahul.instagram.common.exceptions.ResourceNotFoundException;
import com.rahul.instagram.notification.NotificationEvent;
import com.rahul.instagram.notification.NotificationProducer;
import com.rahul.instagram.notification.NotificationType;
import com.rahul.instagram.user.User;
import com.rahul.instagram.user.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserNodeRepository userNodeRepository;
    private final UserRepository userRepository;

    // new for kafka notification
    private final NotificationProducer notificationProducer;

    @Transactional(transactionManager = "neo4jTransactionManager")
    public void followUser(Long currentUserId, Long targetUserId){
        if (currentUserId.equals(targetUserId)){
            throw new IllegalArgumentException("You cannot follow yourself");
        }

        User currentUser =userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Target user not found"));


        userNodeRepository.findById(currentUserId)
                        .orElseGet(() -> userNodeRepository.save(
                                UserNode.builder()
                                        .userId(currentUserId)
                                        .username(currentUser.getUsername())
                                        .build()
                        ));

        userNodeRepository.findById(targetUserId)
                .orElseGet(() -> userNodeRepository.save(
                        UserNode.builder()
                                .userId(targetUserId)
                                .username(targetUser.getUsername())
                                .build()
                ));

        userNodeRepository.followUser(currentUserId, targetUserId);

        NotificationEvent event = new NotificationEvent(
                targetUserId,
                currentUser.getUsername(),
                NotificationType.FOLLOW,
                currentUser.getUsername() + " started following you"
            );
            notificationProducer.sendNotification(event);

    }

    @Transactional(transactionManager = "neo4jTransactionManager")
    public void unfollowUser(Long currentUserId, Long targetUserId){
        userNodeRepository.unfollowUser(currentUserId, targetUserId);
    }

    @Transactional(transactionManager = "neo4jTransactionManager", readOnly = true)
    public List<UserNode> getFollowing(Long userId){
        return userNodeRepository.getFollowing(userId);
    }

    @Transactional(transactionManager = "neo4jTransactionManager", readOnly = true)
    public List<UserNode> getFollowers(Long userId){
        return userNodeRepository.getFollowers(userId);
    }

    @Transactional(transactionManager = "neo4jTransactionManager", readOnly = true)
    public List<UserNode> getMutualFollowing(Long userId1, Long userId2){
        return userNodeRepository.getMutualFollowing(userId1, userId2);
    }

}
