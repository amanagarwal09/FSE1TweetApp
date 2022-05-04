package com.tweetapp.producer;

import com.tweetapp.entity.TweetEntity;
import com.tweetapp.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TweetProducerTest {

    @InjectMocks
    TweetProducer tweetProducer;

    @Mock
    KafkaTemplate<String, TweetEntity> kafkaTemplate;

    @Test
    void testSendMessage() {
        tweetProducer.sendMessage(TestUtil.sampleTweetEntityList().get(0));
        verify(kafkaTemplate, times(1)).send(Mockito.any(), Mockito.any(TweetEntity.class));
    }

}
