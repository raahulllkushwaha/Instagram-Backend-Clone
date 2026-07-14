package com.rahul.instagram.post;

import com.rahul.instagram.common.exceptions.ResourceNotFoundException;
import com.rahul.instagram.media.MediaService;
import com.rahul.instagram.post.dto.CreatePostRequest;
import com.rahul.instagram.post.dto.PostResponse;
import com.rahul.instagram.user.User;
import com.rahul.instagram.user.UserRepository;
import com.rahul.instagram.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserRepository userRepository;
    private final MediaService mediaService;

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

    public PostResponse getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        return postMapper.toPostResponse(post);
    }

    public List<PostResponse> getUserPosts (String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Username not fount"));

        List<Post> posts = postRepository.findByUserOrderByCreatedAtDesc(user);

        return posts.stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());
    }

   public void deletePost(String currentUsername, Long postId){
       Post post = postRepository.findById(postId)
               .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

       if(!post.getUser().getUsername().equals(currentUsername)){
           throw new IllegalArgumentException("You are not authorized to delete this post");
       }

       // to delete post from cloudinary
       if (post.getMediaPublicId() != null) {
           mediaService.deleteFile(post.getMediaPublicId());
       }

       postRepository.deleteById(postId);
   }

}
