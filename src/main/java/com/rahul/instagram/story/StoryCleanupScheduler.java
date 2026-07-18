package com.rahul.instagram.story;

import com.rahul.instagram.media.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StoryCleanupScheduler {

    private final StoryRepository storyRepository;
    private final MediaService mediaService;

    @Scheduled(fixedRate = 3600000)
    public void deleteExpiredStories(){
        List<Story> expiredStory = storyRepository.findByExpiresAtBefore(LocalDateTime.now());

        for(Story story : expiredStory){
            if(story.getMediaPublicId() != null){
                mediaService.deleteFile(story.getMediaPublicId());
            }
        }

        storyRepository.deleteAll(expiredStory);
    }
}
