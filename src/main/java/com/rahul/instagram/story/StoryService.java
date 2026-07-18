package com.rahul.instagram.story;

import com.rahul.instagram.common.exceptions.ResourceNotFoundException;
import com.rahul.instagram.follow.FollowService;
import com.rahul.instagram.follow.UserNode;
import com.rahul.instagram.media.MediaService;
import com.rahul.instagram.story.dto.StoryResponse;
import com.rahul.instagram.user.User;
import com.rahul.instagram.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoryService {

    private final StoryRepository storyRepository;
    private final UserRepository userRepository;
    private final StoryMapper storyMapper;
    private final MediaService mediaService;
    private final FollowService followService;

    public StoryResponse createStory(String currentUsername, MultipartFile file){

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        MediaService.UploadResult uploadResult = mediaService.uploadFile(file);

        Story story =  Story.builder()
                .user(user)
                .mediaUrl(uploadResult.url())
                .mediaPublicId(uploadResult.publicId())
                .build();

        Story savedStory = storyRepository.save(story);
        return storyMapper.toStoryResponse(savedStory);
    }

    public List<StoryResponse> getActiveStoriesOfUser(String username){

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        List<Story> allStories = storyRepository.findByUserOrderByCreatedAtDesc(user);

        return allStories.stream()
                .filter(story -> story.getExpiresAt().isAfter(LocalDateTime.now()))
                .map(storyMapper::toStoryResponse)
                .toList();
    }

    public List<StoryResponse> getFeedStories(String currentUsername){

        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        List<UserNode> followingNodes = followService.getFollowing(currentUser.getId());

        List<Long> userIds = new ArrayList<>();
        userIds.add(currentUser.getId()); // to see out own stories

        if (followingNodes != null && !followingNodes.isEmpty()) {
            userIds.addAll(followingNodes.stream().map(UserNode::getUserId).toList());
        }

        List<Story> stories = storyRepository.findByUser_IdInOrderByCreatedAtDesc(userIds);

        return stories.stream()
                .filter(story -> story.getExpiresAt().isAfter(LocalDateTime.now()))
                .map(storyMapper::toStoryResponse)
                .toList();
    }
}
