package com.rahul.instagram.feed;

import com.rahul.instagram.common.exceptions.ResourceNotFoundException;
import com.rahul.instagram.follow.FollowService;
import com.rahul.instagram.follow.UserNode;
import com.rahul.instagram.post.Post;
import com.rahul.instagram.post.PostMapper;
import com.rahul.instagram.post.PostRepository;
import com.rahul.instagram.post.dto.PostResponse;
import com.rahul.instagram.user.User;
import com.rahul.instagram.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FollowService followService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;


    public Page<PostResponse> getFeed(String currentUsername, Pageable pageable) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + currentUsername));

        List<UserNode> followingNodes = followService.getFollowing(currentUser.getId());

        List<Long> userIds = new ArrayList<>();
        userIds.add(currentUser.getId());

        if (followingNodes != null && !followingNodes.isEmpty()) {
            userIds.addAll(followingNodes.stream().map(UserNode::getUserId).toList());
        }

        return postRepository.findByUser_IdInOrderByCreatedAtDesc(userIds, pageable)
                .map(postMapper::toPostResponse);
    }
}
