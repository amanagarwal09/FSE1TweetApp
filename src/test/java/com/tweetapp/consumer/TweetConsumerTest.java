package com.tweetapp.consumer;

import com.tweetapp.entity.TweetEntity;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TweetConsumerTest {

    @InjectMocks
    TweetConsumer tweetConsumer;

    @Mock
    TweetRepository tweetRepository;

    @Test
    void testConsume() {
        when(tweetRepository.save(Mockito.any(TweetEntity.class))).thenReturn(TestUtil.sampleTweetEntityList().get(0));
        tweetConsumer.consume(TestUtil.sampleTweetEntityList().get(0));
        verify(tweetRepository, times(1)).save(Mockito.any(TweetEntity.class));
    }

}
