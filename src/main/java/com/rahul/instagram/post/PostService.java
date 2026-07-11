package com.rahul.instagram.post;

import com.rahul.instagram.common.exceptions.ResourceNotFoundException;
import com.rahul.instagram.post.dto.CreatePostRequest;
import com.rahul.instagram.post.dto.PostResponse;
import com.rahul.instagram.user.User;
import com.rahul.instagram.user.UserRepository;
import com.rahul.instagram.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserRepository userRepository;

    public PostResponse createPost(String currentUsername, CreatePostRequest request){
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Username not found"));

        Post post = Post.builder()
                .caption(request.getCaption())
                .mediaUrl(request.getMediaUrl())
                .user(user)
                .build();


        Post savedPost = postRepository.save(post);

        PostResponse response = postMapper.toPostResponse(savedPost);
        return response;
    }
}
